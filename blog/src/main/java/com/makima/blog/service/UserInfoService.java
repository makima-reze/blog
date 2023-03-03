package com.makima.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.makima.blog.entity.UserInfo;
import com.makima.blog.vo.UserInfoVO;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

/**
 * @author dai17
 * @create 2022-12-24 16:42
 */
public interface UserInfoService extends IService<UserInfo> {

    void updateUserInfo(@Valid UserInfoVO userInfoVO);

    String updateUserAvatar(MultipartFile file) throws IOException;

}
