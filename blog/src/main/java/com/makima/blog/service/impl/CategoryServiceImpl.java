package com.makima.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.makima.blog.dao.CategoryDao;
import com.makima.blog.dto.CategoryDTO;
import com.makima.blog.dto.CategoryOptionDTO;
import com.makima.blog.entity.Category;
import com.makima.blog.service.CategoryService;
import com.makima.blog.util.BeanCopyUtils;
import com.makima.blog.vo.ConditionVO;
import com.makima.blog.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import java.util.List;

/**
 * @author dai17
 * @create 2022-12-26 11:13
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryDao,Category> implements CategoryService {

    @Autowired
    private CategoryDao categoryDao;

    @Override
    public List<CategoryOptionDTO> listCategoriesBySearch(ConditionVO condition) {
        List<Category> categoryList = categoryDao.selectList(new QueryWrapper<Category>()
                .like(StringUtils.isNotBlank(condition.getKeywords()), "category_name", condition.getKeywords())
                .orderByDesc("id"));
        return BeanCopyUtils.copyList(categoryList,CategoryOptionDTO.class);
    }

    @Override
    public PageResult<CategoryDTO> listCategories() {
        return new PageResult<>(categoryDao.listCategoryDTO(),categoryDao.selectCount(null));
    }
}
