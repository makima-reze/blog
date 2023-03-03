package com.makima.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.makima.blog.dto.TagBackDTO;
import com.makima.blog.dto.TagDTO;
import com.makima.blog.entity.Tag;
import com.makima.blog.vo.ConditionVO;
import com.makima.blog.vo.PageResult;

import java.util.List;

/**
 * @author dai17
 * @create 2022-12-26 0:13
 */
public interface TagService extends IService<Tag> {
    List<TagDTO> listTagsBySearch(ConditionVO condition);

    PageResult<TagDTO> listTags();

}
