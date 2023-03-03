package com.makima.blog.controller;

import com.makima.blog.dto.CommentDTO;
import com.makima.blog.service.CommentService;
import com.makima.blog.vo.CommentVO;
import com.makima.blog.vo.PageResult;
import com.makima.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author dai17
 * @create 2022-12-28 13:38
 */
@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/comments")
    public Result<PageResult<CommentDTO>> listComments(CommentVO commentVO) {
        return Result.ok(commentService.listComments(commentVO));
    }

    @PostMapping("/comments")
    public Result<?> saveComment(@Valid @RequestBody CommentVO commentVO) {
        commentService.saveComment(commentVO);
        return Result.ok();
    }

}
