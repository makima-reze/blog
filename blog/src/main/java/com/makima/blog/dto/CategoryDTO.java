package com.makima.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author dai17
 * @create 2022-12-28 10:59
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDTO {

    private Integer id;

    private String categoryName;

    private Integer articleCount;
}
