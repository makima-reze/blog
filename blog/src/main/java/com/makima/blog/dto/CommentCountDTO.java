package com.makima.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.omg.CORBA.INTERNAL;

/**
 * @author dai17
 * @create 2022-12-29 10:24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentCountDTO {

    private Integer id;

    private Integer commentCount;

}
