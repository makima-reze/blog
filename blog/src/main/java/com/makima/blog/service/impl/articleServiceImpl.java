package com.makima.blog.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.makima.blog.dao.ArticleDao;
import com.makima.blog.dao.ArticleTagDao;
import com.makima.blog.dao.CategoryDao;
import com.makima.blog.dao.TagDao;
import com.makima.blog.dto.*;
import com.makima.blog.entity.*;
import com.makima.blog.exception.BizException;
import com.makima.blog.service.ArticleService;
import com.makima.blog.service.ArticleTagService;
import com.makima.blog.service.BlogInfoService;
import com.makima.blog.service.TagService;
import com.makima.blog.util.BeanCopyUtils;
import com.makima.blog.util.CommonUtils;
import com.makima.blog.util.PageUtils;
import com.makima.blog.vo.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.makima.blog.constant.ArticleStatusEnum.DRAFT;
import static com.makima.blog.constant.ArticleStatusEnum.PUBLIC;
import static com.makima.blog.constant.CommonConst.*;
import static com.makima.blog.constant.RedisPrefixConst.ARTICLE_LIKE_COUNT;
import static com.makima.blog.constant.RedisPrefixConst.ARTICLE_USER_LIKE;
import static com.makima.blog.constant.RedisPrefixConst.ARTICLE_VIEWS_COUNT;

/**
 * @author dai17
 * @create 2022-12-18 20:26
 */
@Service
public class articleServiceImpl extends ServiceImpl<ArticleDao,Article> implements ArticleService {

    @Resource
    private ArticleDao articleDao;

    @Resource
    private CategoryDao categoryDao;

    @Resource
    private ArticleTagDao articleTagDao;

    @Autowired
    private TagDao tagDao;

    @Autowired
    private HttpSession session;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private BlogInfoService blogInfoService;

    @Autowired
    private TagService tagService;

    @Autowired
    private ArticleTagService articleTagService;


    @Override
    public List<ArticleHomeDTO> listArticles() {
        return articleDao.listArticles(PageUtils.getLimitCurrent(),PageUtils.getSize());
    }

    @Override
    public ArticleDTO getArticleById(Integer articleId) {
        //????????????
        //??????????????????
        CompletableFuture<List<ArticleRecommendDTO>> recommendArticleList = CompletableFuture.supplyAsync(() -> articleDao.listRecommendArticles(articleId));
        //??????????????????
        CompletableFuture<List<ArticleRecommendDTO>> newestArticleList = CompletableFuture.supplyAsync(() -> {
            List<Article> articleList = articleDao.selectList(new QueryWrapper<Article>()
                    .select("id", "article_title", "article_cover", "create_time")
                    .eq("is_delete", 0)
                    .eq("status", PUBLIC.getStatus()).orderByDesc("id").last("limit 5"));
            return BeanCopyUtils.copyList(articleList, ArticleRecommendDTO.class);
        });
        ArticleDTO article = articleDao.getArticleById(articleId);
        if (Objects.isNull(article)){
            throw new BizException("???????????????");
        }
        updateArticleViewsCount(articleId);
        //?????????????????????????????????
        Article lastArticle = articleDao.selectOne(new QueryWrapper<Article>()
                .select("id", "article_title", "article_cover")
                .eq("is_delete", 0).eq("status", PUBLIC.getStatus())
                .lt("id", articleId)
                .orderByDesc("id").last("limit 1"));
        Article nextArticle = articleDao.selectOne(new QueryWrapper<Article>()
                .select("id", "article_title", "article_cover")
                .eq("is_delete", 0).eq("status", PUBLIC.getStatus())
                .gt("id", articleId)
                .orderByAsc("id").last("limit 1"));
        article.setLastArticle(BeanCopyUtils.copyObject(lastArticle, ArticlePaginationDTO.class));
        article.setNextArticle(BeanCopyUtils.copyObject(nextArticle, ArticlePaginationDTO.class));
        Double score = redisTemplate.opsForZSet().score(ARTICLE_VIEWS_COUNT, articleId);
        if (Objects.nonNull(score)) {
            article.setViewsCount(score.intValue());
        }
        article.setLikeCount((Integer) redisTemplate.opsForHash().get(ARTICLE_LIKE_COUNT, articleId.toString()));
        // ??????????????????
        try {
            //????????????get????????????
            article.setRecommendArticleList(recommendArticleList.get());
            article.setNewestArticleList(newestArticleList.get());
        } catch (Exception e) {
            log.error("????????????????????????");
        }
        return article;

    }

    @Override
    public void saveOrUpdateArticle(@Valid ArticleVO articleVO) {
        //?????????????????????????????????
        CompletableFuture<WebsiteConfigVO> webConfig = CompletableFuture.supplyAsync(() -> blogInfoService.getWebsiteConfig());
        //??????????????????
        Category category = saveArticleCategory(articleVO);
        Article article = BeanCopyUtils.copyObject(articleVO, Article.class);
        if (Objects.nonNull(category)){
            article.setCategoryId(category.getId());
        }
        if (StrUtil.isBlank(article.getArticleCover())){
            try {
                article.setArticleCover(webConfig.get().getArticleCover());
            } catch (Exception e) {
                throw new BizException("??????????????????????????????");
            }
        }
        //??????????????????
        UserAuth usersEntity = (UserAuth) SecurityUtils.getSubject().getPrincipal();
        article.setUserId(usersEntity.getUserInfoId());
        article.setCreateTime(LocalDateTime.now());
        this.saveOrUpdate(article);
        // ??????????????????
        saveArticleTag(articleVO, article.getId());

    }

    @Override
    public PageResult<ArticleBackDTO> listArticleBacks(ConditionVO condition) {
        Integer count = articleDao.countArticleBacks(condition);
        if (count==0){
            return new PageResult<>();
        }
        // ??????????????????
        List<ArticleBackDTO> articleBackDTOList = articleDao.listArticleBacks(PageUtils.getLimitCurrent(), PageUtils.getSize(), condition);
        return new PageResult<>(articleBackDTOList, count);

    }

    @Override
    public void deleteArticles(List<Integer> articleIdList) {
        articleTagDao.delete(new QueryWrapper<ArticleTag>().in("article_id",articleIdList));
        articleDao.deleteBatchIds(articleIdList);
    }

    @Override
    public ArticleVO getArticleBackById(Integer articleId) {
        // ??????????????????
        Article article = articleDao.selectById(articleId);
        // ??????????????????
        Category category = categoryDao.selectById(article.getCategoryId());
        String categoryName = null;
        if (Objects.nonNull(category)) {
            categoryName = category.getCategoryName();
        }
        // ??????????????????
        List<String> tagNameList = tagDao.listTagNameByArticleId(articleId);
        // ????????????
        ArticleVO articleVO = BeanCopyUtils.copyObject(article, ArticleVO.class);
        articleVO.setCategoryName(categoryName);
        articleVO.setTagNameList(tagNameList);
        return articleVO;
    }

    @Override
    public PageResult<ArchiveDTO> listArchives() {
        Page<Article> page = new Page<>(PageUtils.getCurrent(), PageUtils.getSize());
        Page<Article> articlePage = articleDao.selectPage(page, new QueryWrapper<Article>()
                .select("id", "article_title", "create_time")
                .orderByDesc("create_time")
                .eq("is_delete", FALSE)
                .eq("status", PUBLIC.getStatus()));
        List<ArchiveDTO> archiveDTOList = BeanCopyUtils.copyList(articlePage.getRecords(), ArchiveDTO.class);
        return new PageResult<>(archiveDTOList,(int)articlePage.getTotal());
    }

    @Override
    public void updateArticleDelete(@Valid DeleteVO deleteVO) {
        // ??????????????????????????????
        List<Article> articleList = deleteVO.getIdList().stream().map(id -> Article.builder()
                .id(id)
                .isTop(FALSE)
                .isDelete(deleteVO.getIsDelete())
                .build())
                .collect(Collectors.toList());
        this.updateBatchById(articleList);
    }

    @Override
    public List<ArticleSearchDTO> listArticleBySearch(ConditionVO condition) {
        String keywords = condition.getKeywords();
        if (StringUtils.isBlank(keywords)){
            return new ArrayList<>();
        }
        List<Article> articleList = articleDao.selectList(new QueryWrapper<Article>()
                .eq("is_delete", FALSE)
                .eq("status", PUBLIC.getStatus())
                .and(i -> i.like("article_title", keywords)
                        .or()
                        .like("article_content", keywords)));
        // ????????????
        return articleList.stream().map(item -> {
            // ???????????????????????????????????????
            String articleContent = item.getArticleContent();
            int index = item.getArticleContent().indexOf(keywords);
            if (index != -1) {
                // ??????????????????????????????
                int preIndex = index > 25 ? index - 25 : 0;
                String preText = item.getArticleContent().substring(preIndex, index);
                // ?????????????????????????????????
                int last = index + keywords.length();
                int postLength = item.getArticleContent().length() - last;
                int postIndex = postLength > 175 ? last + 175 : last + postLength;
                String postText = item.getArticleContent().substring(index, postIndex);
                // ??????????????????
                articleContent = (preText + postText).replaceAll(keywords, PRE_TAG + keywords + POST_TAG);
            }
            // ??????????????????
            String articleTitle = item.getArticleTitle().replaceAll(keywords, PRE_TAG + keywords + POST_TAG);
            return ArticleSearchDTO.builder()
                    .id(item.getId())
                    .articleTitle(articleTitle)
                    .articleContent(articleContent)
                    .build();
        }).collect(Collectors.toList());
    }

    @Override
    public ArticlePreviewListDTO listArticlesByCondition(ConditionVO condition) {
        // ????????????
        List<ArticlePreviewDTO> articlePreviewDTOList = articleDao.listArticlesByCondition(PageUtils.getLimitCurrent(), PageUtils.getSize(), condition);
        // ?????????????????????(??????????????????)
        String name;
        if (Objects.nonNull(condition.getCategoryId())) {
            name = categoryDao.selectOne(new LambdaQueryWrapper<Category>().select(Category::getCategoryName)
                    .eq(Category::getId, condition.getCategoryId())).getCategoryName();
        } else {
            name = tagService.getOne(new LambdaQueryWrapper<Tag>()
                    .select(Tag::getTagName).eq(Tag::getId, condition.getTagId())).getTagName();
        }
        return ArticlePreviewListDTO.builder().articlePreviewDTOList(articlePreviewDTOList).name(name).build();

    }

    @Override
    public void saveArticleLike(Integer articleId) {
        // ??????????????????
        UserAuth usersEntity = (UserAuth) SecurityUtils.getSubject().getPrincipal();
        String articleLikeKey = ARTICLE_USER_LIKE + usersEntity.getUserInfoId();
        if (!redisTemplate.opsForSet().isMember(articleLikeKey, articleId)) {
            // ????????????????????????id
            redisTemplate.opsForSet().add(articleLikeKey, articleId);
            // ???????????????+1
            redisTemplate.opsForHash().increment(ARTICLE_LIKE_COUNT, articleId.toString(), 1L);
        }
    }


    public void updateArticleViewsCount(Integer articleId) {
        // ?????????????????????????????????????????????
        Set<Integer> articleSet = CommonUtils.castSet(Optional.ofNullable(session.getAttribute(ARTICLE_SET)).orElseGet(HashSet::new), Integer.class);
        if (!articleSet.contains(articleId)) {
            articleSet.add(articleId);
            session.setAttribute(ARTICLE_SET, articleSet);
            // ?????????+1
            redisTemplate.opsForZSet().incrementScore(ARTICLE_VIEWS_COUNT, articleId, 1D);
        }
    }

    private Category saveArticleCategory(ArticleVO articleVO){
        //????????????
        Category category = categoryDao.selectOne(new QueryWrapper<Category>().eq("category_name", articleVO.getCategoryName()));
        if (Objects.isNull(category)&&!articleVO.getStatus().equals(DRAFT.getStatus())){
            category = Category.builder().categoryName(articleVO.getCategoryName()).build();
            categoryDao.insert(category);
        }
        return category;
    }

    private void saveArticleTag(ArticleVO articleVO,Integer articleId){
        if (Objects.nonNull(articleVO.getId())){
            articleTagDao.delete(new QueryWrapper<ArticleTag>().eq("article_id",articleVO.getId()));
        }
        List<String> tagNameList = articleVO.getTagNameList();
        if (CollectionUtils.isNotEmpty(tagNameList)){
            List<Tag> existTagList = tagService.list(new QueryWrapper<Tag>().in("tag_name", tagNameList));
            List<String> existTagNameList = existTagList.stream().map(Tag::getTagName).collect(Collectors.toList());
            List<Integer> existTagIdList = existTagList.stream().map(Tag::getId).collect(Collectors.toList());
            tagNameList.removeAll(existTagNameList);
            if (CollectionUtils.isNotEmpty(tagNameList)){
                List<Tag> tagList = tagNameList.stream().map(item -> Tag.builder().tagName(item).build()).collect(Collectors.toList());
                tagService.saveBatch(tagList);
                List<Integer> tagIdList = tagList.stream().map(Tag::getId).collect(Collectors.toList());
                existTagIdList.addAll(tagIdList);
            }
            // ????????????id????????????
            List<ArticleTag> articleTagList = existTagIdList.stream().map(item -> ArticleTag.builder()
                    .articleId(articleId)
                    .tagId(item)
                    .build())
                    .collect(Collectors.toList());
            articleTagService.saveBatch(articleTagList);
        }
    }


}
