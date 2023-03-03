package com.makima.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author dai17
 * @create 2022-12-26 0:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("tb_article_tag")
public class ArticleTag {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    private Integer articleId;

    private Integer tagId;

}
