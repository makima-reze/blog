package com.makima.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.makima.blog.constant.CommonConst;
import com.makima.blog.constant.RoleEnum;
import com.makima.blog.dao.UserInfoDao;
import com.makima.blog.dao.UserAuthDao;
import com.makima.blog.dao.UserRoleDao;
import com.makima.blog.dto.UserInfoDTO;
import com.makima.blog.entity.UserAuth;
import com.makima.blog.entity.UserInfo;
import com.makima.blog.entity.UserRole;
import com.makima.blog.exception.BizException;
import com.makima.blog.service.BlogInfoService;
import com.makima.blog.service.UserAuthService;
import com.makima.blog.shiro.ShiroMD5;
import com.makima.blog.util.BeanCopyUtils;
import com.makima.blog.vo.UserVO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author dai17
 * @create 2022-12-21 21:54
 */
@Service
public class UserAuthServiceImpl extends ServiceImpl<UserAuthDao,UserAuth> implements UserAuthService {

    @Resource
    private UserInfoDao userInfoDao;

    @Resource
    private UserAuthDao userAuthDao;

    @Resource
    private UserRoleDao userRoleDao;


    @Autowired
    private BlogInfoService blogInfoService;

    @Override
    public UserInfoDTO login(@Valid UserVO userVO) {
        @NotBlank(message = "邮箱不能为空") @Email(message = "邮箱格式不正确") String username = userVO.getUsername();
        @Size(min = 6, message = "密码不能少于6位") @NotBlank(message = "密码不能为空") String password = userVO.getPassword();
        //Subject的login(AuthenticationToken token)方法，其调用的为DelegatingSubject类的login方法，DelegatingSubject实现了Subject接口
        //Subject subject = securityManager.login(this, token); 注意到其调用了SecurityManager的login方法，SecurityManager为接口，
        // 实际上调用的其实现类DefaultSecurityManager的login方法

        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        System.out.println("封装输入的用户名和密码成功");
        try{
            subject.login(token);
            UserAuth usersEntity = (UserAuth) subject.getPrincipal();
            SimpleHash s = (SimpleHash) ShiroMD5.MD5(username, password);
            if(s.toHex().equals(usersEntity.getPassword())){
                //密码输入正确
                UserInfo userInfo = userInfoDao.selectOne(new QueryWrapper<UserInfo>().eq("id", usersEntity.getUserInfoId()));
                UserInfoDTO userLoginDTO = BeanCopyUtils.copyObject(userInfo, UserInfoDTO.class);
                UserRole userRole = userRoleDao.selectOne(new QueryWrapper<UserRole>().eq("user_id", usersEntity.getUserInfoId()));
                userLoginDTO.setRoleId(userRole.getRoleId());
                return userLoginDTO;
            }else {
                return null;
            }
        }catch (Exception e){
            return null;
        }

    }

    //声明式事务，保证方法内多个数据库操作要么同时成功、要么同时失败
    @Transactional
    @Override
    public void register(@Valid UserVO userVO) {
        //验证账户是否被注册过
        if (checkUser(userVO)){
            throw new BizException("账户已经被注册");
        }
        UserInfo userInfo = UserInfo.builder().email(userVO.getUsername())
                .nickname(CommonConst.DEFAULT_NICKNAME + IdWorker.getId())
                .avatar(blogInfoService.getWebsiteConfig().getUserAvatar())
                .createTime(LocalDateTime.now())
                .build();
        userInfoDao.insert(userInfo);
        UserRole userRole = UserRole.builder()
                .userId(userInfo.getId())
                .roleId(RoleEnum.USER.getRoleId())
                .build();
        userRoleDao.insert(userRole);
        UserAuth userAuth = UserAuth.builder()
                .userInfoId(userInfo.getId())
                .username(userVO.getUsername())
                .password(ShiroMD5.MD5(userVO.getUsername(),userVO.getPassword()).toString())
                .loginType(1)
                .createTime(LocalDateTime.now())
                .build();
        userAuthDao.insert(userAuth);

    }

    private Boolean checkUser(UserVO userVO){
        if(!userVO.getCode().equals(userVO.getIdentify())){
            throw new BizException("验证码错误！");
        }
        UserAuth userAuth = userAuthDao.selectOne(new QueryWrapper<UserAuth>().eq("username", userVO.getUsername()));
        return Objects.nonNull(userAuth);

    }
}
