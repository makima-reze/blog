package com.makima.blog.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @author dai17
 * @create 2022-12-19 11:52
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("tb_talk")
public class Talk {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    private String content;

    private String images;

    private Integer isTop;

    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

}
