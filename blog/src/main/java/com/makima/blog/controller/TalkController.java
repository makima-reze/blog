package com.makima.blog.controller;

import com.makima.blog.dto.TalkDTO;
import com.makima.blog.service.TalkService;
import com.makima.blog.vo.PageResult;
import com.makima.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author dai17
 * @create 2022-12-19 11:50
 */
@RestController
public class TalkController {

    @Autowired
    private TalkService talkService;

    @GetMapping("/home/talks")
    public Result<List<String>> listHomeTalks() {
        return Result.ok(talkService.listHomeTalks());
    }

    @GetMapping("/talks")
    public Result<PageResult<TalkDTO>> listTalks() {
        return Result.ok(talkService.listTalks());
    }

}
