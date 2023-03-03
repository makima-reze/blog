package com.makima.blog.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author dai17
 * @create 2022-12-18 10:36
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("tb_tag")
public class Tag {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    private String tagName;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;


}
