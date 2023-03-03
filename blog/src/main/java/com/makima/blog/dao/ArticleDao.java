package com.makima.blog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.makima.blog.dto.*;
import com.makima.blog.entity.Article;
import com.makima.blog.vo.ConditionVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleDao extends BaseMapper<Article> {


    List<ArticleHomeDTO> listArticles(@Param("current") Long limitCurrent, @Param("size") Long size);

    List<ArticleRecommendDTO> listRecommendArticles(@Param("articleId") Integer articleId);

    ArticleDTO getArticleById(@Param("articleId") Integer articleId);

    Integer countArticleBacks(@Param("condition") ConditionVO condition);

    List<ArticleBackDTO> listArticleBacks(@Param("current") Long current, @Param("size") Long size, @Param("condition") ConditionVO condition);

    List<ArticlePreviewDTO> listArticlesByCondition(@Param("current") Long current, @Param("size") Long size, @Param("condition") ConditionVO condition);

}
