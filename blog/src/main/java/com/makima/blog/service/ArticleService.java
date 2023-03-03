package com.makima.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.makima.blog.dto.*;
import com.makima.blog.entity.Article;
import com.makima.blog.vo.ArticleVO;
import com.makima.blog.vo.ConditionVO;
import com.makima.blog.vo.DeleteVO;
import com.makima.blog.vo.PageResult;

import javax.validation.Valid;
import java.util.List;

/**
 * @author dai17
 * @create 2022-12-18 20:26
 */
public interface ArticleService  extends IService<Article>{
    List<ArticleHomeDTO> listArticles();

    ArticleDTO getArticleById(Integer articleId);

    void saveOrUpdateArticle(@Valid ArticleVO articleVO);

    PageResult<ArticleBackDTO> listArticleBacks(ConditionVO condition);

    void deleteArticles(List<Integer> articleIdList);

    ArticleVO getArticleBackById(Integer articleId);

    PageResult<ArchiveDTO> listArchives();

    void updateArticleDelete(@Valid DeleteVO deleteVO);

    List<ArticleSearchDTO> listArticleBySearch(ConditionVO condition);

    ArticlePreviewListDTO listArticlesByCondition(ConditionVO condition);

    void saveArticleLike(Integer articleId);

}
