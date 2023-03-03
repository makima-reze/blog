package com.makima.blog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.makima.blog.entity.Message;
import org.springframework.stereotype.Repository;

/**
 * @author dai17
 * @create 2022-12-29 11:02
 */
@Repository
public interface MessageDao extends BaseMapper<Message> {
}
