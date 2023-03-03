package com.makima.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author dai17
 * @create 2022-12-18 20:22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TagDTO {

    private Integer id;

    private String tagName;

}
