package com.makima.blog.vo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 逻辑删除
 *
 * @author yezhiqiu
 * @date 2021/07/28
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeleteVO {

    /**
     * id列表
     */
    @NotNull(message = "id不能为空")
    private List<Integer> idList;

    /**
     * 状态值
     */
    @NotNull(message = "状态值不能为空")
    private Integer isDelete;

}
