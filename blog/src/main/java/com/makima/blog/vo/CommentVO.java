package com.makima.blog.vo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 评论
 *
 * @author yezhiqiu
 * @date 2021/08/10
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentVO {

    /**
     * 回复用户id
     */
    private Integer replyUserId;

    /**
     * 评论主题id
     */
    private Integer topicId;

    /**
     * 评论内容
     */
    @NotBlank(message = "评论内容不能为空")
    private String commentContent;

    /**
     * 父评论id
     */
    private Integer parentId;

    /**
     * 类型
     */
    @NotNull(message = "评论类型不能为空")
    private Integer type;

}
