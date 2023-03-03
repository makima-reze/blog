package com.makima.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.makima.blog.dto.CategoryDTO;
import com.makima.blog.dto.CategoryOptionDTO;
import com.makima.blog.entity.Category;
import com.makima.blog.vo.ConditionVO;
import com.makima.blog.vo.PageResult;

import java.util.List;

/**
 * @author dai17
 * @create 2022-12-26 11:11
 */
public interface CategoryService extends IService<Category> {
    List<CategoryOptionDTO> listCategoriesBySearch(ConditionVO condition);

    PageResult<CategoryDTO> listCategories();

}
