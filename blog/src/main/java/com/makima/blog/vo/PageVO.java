package com.makima.blog.vo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;


/**
 * 页面信息
 *
 * @author yezhiqiu
 * @date 2021/08/07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageVO {

    /**
     * 页面id
     */
    private Integer id;

    /**
     * 页面名
     */
    @NotBlank(message = "页面名称不能为空")
    private String pageName;

    /**
     * 页面标签
     */
    @NotBlank(message = "页面标签不能为空")
    private String pageLabel;

    /**
     * 页面封面
     */
    @NotBlank(message = "页面封面不能为空")
    private String pageCover;

}
