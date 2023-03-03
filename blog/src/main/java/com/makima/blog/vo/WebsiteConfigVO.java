package com.makima.blog.vo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 网站配置信息
 *
 * @author yezhiqiu
 * @date 2021/08/09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WebsiteConfigVO {

    /**
     * 网站头像
     */
    private String websiteAvatar;

    /**
     * 网站名称
     */
    private String websiteName;

    /**
     * 网站作者
     */
    private String websiteAuthor;

    /**
     * 网站介绍
     */
    private String websiteIntro;

    /**
     * 网站公告
     */
    private String websiteNotice;

    /**
     * 网站创建时间
     */
    private String websiteCreateTime;

    /**
     * 网站备案号
     */
    private String websiteRecordNo;

    /**
     * 社交登录列表
     */
    private List<String> socialLoginList;

    /**
     * 社交url列表
     */
    private List<String> socialUrlList;

    /**
     * qq
     */
    private String qq;

    /**
     * github
     */
    private String github;

    /**
     * gitee
     */
    private String gitee;

    /**
     * 游客头像
     */
    private String touristAvatar;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 是否评论审核
     */
    private Integer isCommentReview;

    /**
     * 是否留言审核
     */
    private Integer isMessageReview;


    /**
     * 文章封面
     */
    private String articleCover;

    /**
     * 是否开启聊天室
     */
    private Integer isChatRoom;

    /**
     * websocket地址
     */
    private String websocketUrl;


}
