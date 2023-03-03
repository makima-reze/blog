package com.makima.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.makima.blog.dao.ArticleDao;
import com.makima.blog.dao.CategoryDao;
import com.makima.blog.dao.TagDao;
import com.makima.blog.dao.WebsiteConfigDao;
import com.makima.blog.dto.BlogHomeInfoDTO;
import com.makima.blog.entity.Article;
import com.makima.blog.service.BlogInfoService;
import com.makima.blog.service.PageService;
import com.makima.blog.vo.PageVO;
import com.makima.blog.vo.WebsiteConfigVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


import static com.makima.blog.constant.ArticleStatusEnum.PUBLIC;
import static com.makima.blog.constant.CommonConst.DEFAULT_CONFIG_ID;
import static com.makima.blog.constant.CommonConst.FALSE;
import static com.makima.blog.constant.RedisPrefixConst.WEBSITE_CONFIG;

@Service
public class blogInfoServiceImpl implements BlogInfoService {

    @Resource
    private ArticleDao articleDao;

    @Resource
    private CategoryDao categoryDao;

    @Resource
    private TagDao tagDao;

    @Resource
    private WebsiteConfigDao websiteConfigDao;

    @Resource
    private RedisTemplate redisTemplate;

    @Autowired
    private PageService pageService;


    @Override
    public BlogHomeInfoDTO getBlogHomeInfo() {
        Integer articleCount = articleDao.selectCount(new QueryWrapper<Article>().eq("status", PUBLIC.getStatus()).eq("is_delete", FALSE));
        Integer categoryCount = categoryDao.selectCount(null);
        Integer tagCount = tagDao.selectCount(null);
        //todo 访问量
        Object count = "0";
        String viewsCount = Optional.ofNullable(count).orElse(0).toString();
        WebsiteConfigVO websiteConfig = this.getWebsiteConfig();
        List<PageVO> pageVOList =  pageService.listPages();

        return BlogHomeInfoDTO.builder().articleCount(articleCount)
                .categoryCount(categoryCount)
                .tagCount(tagCount)
                .viewsCount(viewsCount)
                .websiteConfig(websiteConfig)
                .pageList(pageVOList)
                .build();

    }

    @Override
    public WebsiteConfigVO getWebsiteConfig() {
        WebsiteConfigVO websiteConfigVO;
        Object websiteConfig = redisTemplate.opsForValue().get(WEBSITE_CONFIG);
        if (Objects.nonNull(websiteConfig)){
            websiteConfigVO = JSON.parseObject(websiteConfig.toString(), WebsiteConfigVO.class);
        }else {
            String config = websiteConfigDao.selectById(DEFAULT_CONFIG_ID).getConfig();
            websiteConfigVO = JSON.parseObject(config, WebsiteConfigVO.class);
            redisTemplate.opsForValue().set(WEBSITE_CONFIG,config);
        }
        return websiteConfigVO;
    }
}
