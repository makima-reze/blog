package com.makima.blog.vo;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;


/**
 * 文章
 *
 * @author yezhiqiu
 * @date 2021/08/03
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleVO {

    /**
     * 文章id
     */
    private Integer id;

    /**
     * 标题
     */
    @NotBlank(message = "文章标题不能为空")
    private String articleTitle;

    /**
     * 内容
     */
    @NotBlank(message = "文章内容不能为空")
    private String articleContent;

    /**
     * 文章封面
     */
    private String articleCover;

    /**
     * 文章分类
     */
    private String categoryName;

    /**
     * 文章标签
     */
    private List<String> tagNameList;

    /**
     * 文章类型
     */
    private Integer type;

    /**
     * 原文链接
     */
    private String originalUrl;

    /**
     * 是否置顶
     */
    private Integer isTop;

    /**
     * 文章状态 1.公开 2.私密 3.评论可见
     */
    private Integer status;

}
