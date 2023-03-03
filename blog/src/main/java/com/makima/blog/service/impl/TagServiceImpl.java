package com.makima.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.makima.blog.dao.TagDao;
import com.makima.blog.dto.TagBackDTO;
import com.makima.blog.dto.TagDTO;
import com.makima.blog.entity.Tag;
import com.makima.blog.service.TagService;
import com.makima.blog.util.BeanCopyUtils;
import com.makima.blog.util.PageUtils;
import com.makima.blog.vo.ConditionVO;
import com.makima.blog.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author dai17
 * @create 2022-12-26 0:13
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagDao,Tag> implements TagService {

    @Autowired
    private TagDao tagDao;

    @Override
    public List<TagDTO> listTagsBySearch(ConditionVO condition) {
        List<Tag> tagList = tagDao.selectList(new QueryWrapper<Tag>()
                .like(StringUtils.isNotBlank(condition.getKeywords()), "tag_name", condition.getKeywords())
                .orderByDesc("id"));
        return BeanCopyUtils.copyList(tagList,TagDTO.class);
    }

    @Override
    public PageResult<TagDTO> listTags() {
        // 查询标签列表
        List<Tag> tagList = tagDao.selectList(null);
        // 转换DTO
        List<TagDTO> tagDTOList = BeanCopyUtils.copyList(tagList, TagDTO.class);
        // 查询标签数量
        Integer count = tagDao.selectCount(null);
        return new PageResult<>(tagDTOList, count);
    }


}
