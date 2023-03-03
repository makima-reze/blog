package com.makima.blog.controller;

import com.makima.blog.dto.TagBackDTO;
import com.makima.blog.dto.TagDTO;
import com.makima.blog.service.TagService;
import com.makima.blog.vo.ConditionVO;
import com.makima.blog.vo.PageResult;
import com.makima.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author dai17
 * @create 2022-12-26 13:48
 */
@RestController
public class TagController {

    @Autowired
    private TagService tagService;


    @GetMapping("/tags/search")
    public Result<List<TagDTO>> listTagBySearch(ConditionVO condition){
        //查询标签
        return Result.ok(tagService.listTagsBySearch(condition));
    }

    @GetMapping("/tags")
    public Result<PageResult<TagDTO>> listTags(){
        return Result.ok(tagService.listTags());
    }


}
