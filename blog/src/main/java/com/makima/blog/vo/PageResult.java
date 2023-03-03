package com.makima.blog.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author dai17
 * @create 2022-12-18 19:19
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageResult<T> {

    private List<T> recordList;

    private Integer count;

}
