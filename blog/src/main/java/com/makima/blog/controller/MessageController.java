package com.makima.blog.controller;

import com.makima.blog.dto.MessageDTO;
import com.makima.blog.service.MessageService;
import com.makima.blog.vo.MessageVO;
import com.makima.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @author dai17
 * @create 2022-12-29 10:57
 */
@RestController
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/messages")
    public Result<?> saveMessage(@Valid @RequestBody MessageVO messageVO) {
        messageService.saveMessage(messageVO);
        return Result.ok();
    }

    @GetMapping("/messages")
    public Result<List<MessageDTO>> listMessages() {
        return Result.ok(messageService.listMessages());
    }




}
