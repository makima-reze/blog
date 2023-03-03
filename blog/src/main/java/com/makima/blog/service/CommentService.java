package com.makima.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.makima.blog.dto.CommentDTO;
import com.makima.blog.entity.Comment;
import com.makima.blog.vo.CommentVO;
import com.makima.blog.vo.PageResult;

import javax.validation.Valid;

/**
 * @author dai17
 * @create 2022-12-28 13:41
 */
public interface CommentService extends IService<Comment> {

    PageResult<CommentDTO> listComments(CommentVO commentVO);

    void saveComment(@Valid CommentVO commentVO);

}
