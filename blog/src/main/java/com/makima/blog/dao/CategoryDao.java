package com.makima.blog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.makima.blog.dto.CategoryDTO;
import com.makima.blog.entity.Category;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author dai17
 * @create 2022-12-18 9:02
 */
@Repository
public interface CategoryDao extends BaseMapper<Category> {

    List<CategoryDTO> listCategoryDTO();

}
