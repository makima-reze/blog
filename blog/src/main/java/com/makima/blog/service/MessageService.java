package com.makima.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.makima.blog.dto.MessageDTO;
import com.makima.blog.entity.Message;
import com.makima.blog.vo.MessageVO;

import javax.validation.Valid;
import java.util.List;

/**
 * @author dai17
 * @create 2022-12-29 11:01
 */
public interface MessageService extends IService<Message> {
    void saveMessage(@Valid MessageVO messageVO);

    List<MessageDTO> listMessages();

}
