package com.makima.blog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.makima.blog.dto.TalkDTO;
import com.makima.blog.entity.Talk;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author dai17
 * @create 2022-12-19 11:56
 */
@Repository
public interface TalkDao extends BaseMapper<Talk> {

    List<TalkDTO> listTalks(@Param("current") Long limitCurrent, @Param("size") Long size);
}
