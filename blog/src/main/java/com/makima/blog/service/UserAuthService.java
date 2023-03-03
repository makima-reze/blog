package com.makima.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.makima.blog.dto.UserInfoDTO;
import com.makima.blog.entity.UserAuth;
import com.makima.blog.vo.Result;
import com.makima.blog.vo.UserInfoVO;
import com.makima.blog.vo.UserVO;

import javax.validation.Valid;

/**
 * @author dai17
 * @create 2022-12-21 21:41
 */
public interface UserAuthService extends IService<UserAuth> {

    UserInfoDTO login(@Valid UserVO userVO);

    void register(@Valid UserVO userVO);

}
