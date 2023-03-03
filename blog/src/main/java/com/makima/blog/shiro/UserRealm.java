package com.makima.blog.shiro;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.makima.blog.entity.UserAuth;
import com.makima.blog.service.UserAuthService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

public class UserRealm extends AuthorizingRealm {

    @Autowired
    UserAuthService userAuthService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //执行subject.login()时会跳转到这
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authenticationToken;
        UserAuth userAuth = userAuthService.getOne(
                new QueryWrapper<UserAuth>()
                        .eq("username", usernamePasswordToken.getUsername()));
        //验证账户是否存在
        if (userAuth==null){
            throw new UnknownAccountException("账户不存在");
        }else {
            ByteSource salt = ByteSource.Util.bytes(userAuth.getUsername());
            //密码加盐传出去进行对比
            return new SimpleAuthenticationInfo(userAuth,userAuth.getPassword(),salt,this.getName());
        }

    }
}
