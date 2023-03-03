package com.makima.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author dai17
 * @create 2022-12-18 19:39
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArticleHomeDTO {
    private Integer id;

    private String articleCover;

    private String articleTitle;

    private String articleContent;

    private LocalDateTime createTime;

    private Integer isTop;

    private Integer type;

    private Integer categoryId;

    private String categoryName;

    private List<TagDTO> tagDTOList;

}
