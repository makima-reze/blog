package com.makima.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.makima.blog.dao.MessageDao;
import com.makima.blog.dto.MessageDTO;
import com.makima.blog.entity.Message;
import com.makima.blog.service.MessageService;
import com.makima.blog.util.BeanCopyUtils;
import com.makima.blog.util.HTMLUtils;
import com.makima.blog.vo.MessageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

import java.time.LocalDateTime;
import java.util.List;

import static com.makima.blog.constant.CommonConst.TRUE;

/**
 * @author dai17
 * @create 2022-12-29 11:02
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageDao,Message> implements MessageService {

    @Autowired
    private MessageDao messageDao;

    @Override
    public void saveMessage(@Valid MessageVO messageVO) {
        Message message = BeanCopyUtils.copyObject(messageVO, Message.class);
        message.setMessageContent(HTMLUtils.filter(message.getMessageContent()));
        message.setIsReview(TRUE);
        message.setCreateTime(LocalDateTime.now());
        message.setIpAddress("127.0.0.1");
        message.setIpSource("");
        messageDao.insert(message);
    }

    @Override
    public List<MessageDTO> listMessages() {
        List<Message> messageList = messageDao.selectList(new QueryWrapper<Message>()
                .select("id", "nickname", "avatar", "message_content", "time")
                .eq("is_review", TRUE));
        return BeanCopyUtils.copyList(messageList,MessageDTO.class);
    }
}
