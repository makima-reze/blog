package com.makima.blog.controller;

import com.makima.blog.dto.CategoryDTO;
import com.makima.blog.dto.CategoryOptionDTO;
import com.makima.blog.service.CategoryService;
import com.makima.blog.vo.ConditionVO;
import com.makima.blog.vo.PageResult;
import com.makima.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dai17
 * @create 2022-12-26 11:10
 */
@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categories/search")
    public Result<List<CategoryOptionDTO>> listCategoriesBySearch(ConditionVO condition){
        ArrayList<Integer> arrayList = new ArrayList<>();
        return Result.ok(categoryService.listCategoriesBySearch(condition));
    }

    @GetMapping("/categories")
    public Result<PageResult<CategoryDTO>> listCategories(){
        return Result.ok(categoryService.listCategories());
    }



}
