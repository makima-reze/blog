package com.makima.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.makima.blog.dto.TalkDTO;
import com.makima.blog.entity.Talk;
import com.makima.blog.vo.PageResult;

import java.util.List;

/**
 * @author dai17
 * @create 2022-12-19 11:51
 */
public interface TalkService extends IService<Talk>{
    List<String> listHomeTalks();

    PageResult<TalkDTO> listTalks();

}
