package com.makima.blog.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author dai17
 * @create 2022-12-18 15:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName(value = "tb_page")
public class Page {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String pageName;

    private String pageLabel;

    private String pageCover;

    @TableField(fill = FieldFill.INSERT)
    private String CreateTime;

    @TableField(fill = FieldFill.UPDATE)
    private String updateTime;

}
