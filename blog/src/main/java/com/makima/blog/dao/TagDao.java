package com.makima.blog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.makima.blog.entity.Tag;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author dai17
 * @create 2022-12-18 10:36
 */
@Repository
public interface TagDao extends BaseMapper<Tag> {

    List<String> listTagNameByArticleId(@Param("articleId") Integer articleId);

}
