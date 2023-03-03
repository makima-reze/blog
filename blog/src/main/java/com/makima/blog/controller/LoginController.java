package com.makima.blog.controller;

import com.alibaba.fastjson.JSON;
import com.makima.blog.dto.UserInfoDTO;
import com.makima.blog.service.UserAuthService;
import com.makima.blog.service.UserInfoService;
import com.makima.blog.vo.Result;
import com.makima.blog.vo.UserInfoVO;
import com.makima.blog.vo.UserVO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

/**
 * @author dai17
 * @create 2022-12-21 21:27
 */
@RestController
public class LoginController {

    @Autowired
    UserAuthService userAuthService;

    @Autowired
    UserInfoService userInfoService;

    @PostMapping(value = "/login",consumes = "application/x-www-form-urlencoded",produces = {"application/json; charset=utf-8"})
    public void Login(@RequestParam String username,@RequestParam String password,HttpServletResponse httpServletResponse) throws IOException {
        UserVO userVO = UserVO.builder().username(username).password(password).build();
        UserInfoDTO login = userAuthService.login(userVO);
        if (login!=null){
            System.out.println("登录成功");
            httpServletResponse.setContentType("application/json;charset=utf-8");
            httpServletResponse.getWriter().write(JSON.toJSONString(Result.ok(login)));
        }else {
            System.out.println("登录失败");
            httpServletResponse.setContentType("application/json;charset=utf-8");
            httpServletResponse.getWriter().write(JSON.toJSONString(Result.fail("账户或者密码错误")));
        }
    }


    @GetMapping("/logout")
    public void Logout(HttpServletResponse httpServletResponse) throws IOException {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        httpServletResponse.setContentType("application/json;charset=utf-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(Result.ok()));
    }

    @PostMapping(value = "/register")
    public Result<?> Register(@Valid @RequestBody UserVO userVO){
        userAuthService.register(userVO);
        return Result.ok();
    }

    @PutMapping("/users/info")
    public Result<?> updateUserInfo(@Valid @RequestBody UserInfoVO userInfoVO){
        userInfoService.updateUserInfo(userInfoVO);
        return Result.ok();
    }

    @PostMapping("/users/avatar")
    public Result<String> updateuserAvatar(MultipartFile file) throws IOException {
        return Result.ok(userInfoService.updateUserAvatar(file));
    }



}
