package com.makima.blog.controller;

import com.makima.blog.dto.BlogHomeInfoDTO;
import com.makima.blog.service.BlogInfoService;
import com.makima.blog.vo.Result;
import com.makima.blog.vo.WebsiteConfigVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class BlogInfoController {

    @Autowired
    BlogInfoService blogInfoService;

    @GetMapping("/")
    public Result<BlogHomeInfoDTO> getBlogHomeInfo() {
        return Result.ok(blogInfoService.getBlogHomeInfo());
    }

}
