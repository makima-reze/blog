package com.makima.blog.config;


import com.makima.blog.shiro.UserRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;

@Configuration
public class ShiroConfig {
    //Shiro 提供了用于加密密码和验证密码服务的 CredentialsMatcher 接口，而 HashedCredentialsMatcher 正是 CredentialsMatcher 的一个实现类
    @Bean("hashedCredentialsMatcher")
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        //指定加密方式为MD5
        credentialsMatcher.setHashAlgorithmName("MD5");
        //加密次数
        credentialsMatcher.setHashIterations(1024);
        //存储散列后密码是否为16进制
        credentialsMatcher.setStoredCredentialsHexEncoded(true);
        return credentialsMatcher;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(
            @Qualifier("defaultWebSecurityManager") WebSecurityManager securityManager
    ){
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        bean.setSecurityManager(securityManager);   // 设置安全管理器
        // 设置登录url映射
        bean.setLoginUrl("login");
        // 设置未授权时的跳转的请求
        bean.setUnauthorizedUrl("/");
        // 添加shiro的内置过滤器
        /*
            anon: 无需认证就可以登录
            authc:必须认证才能登录
            user: 必须拥有“记住我”这个功能
            perms:拥有对某个资源的权限才能访问
            role:拥有某个角色才能访问
         */
        LinkedHashMap<String, String> filterMap = new LinkedHashMap<>();    //使用LinkedHashMap可以保证顺序 以便 /** anon在最后过滤
        // 权限授权,访问url需要权限,支持通配符
        filterMap.put("/", "anon");
        filterMap.put("/user", "authc");    // authc --   认证(登录)才能使用
        filterMap.put("/editor", "roles[admin]");
        filterMap.put("/SuperAdmin", "roles[admin]");
        filterMap.put("/druid/**", "anon");
        filterMap.put("/**", "anon");
        //设置网站过滤定义的map
        bean.setFilterChainDefinitionMap(filterMap);
        return bean;
    }

    //2. 获取安全管理器
    @Bean(name = "defaultWebSecurityManager")
    public DefaultWebSecurityManager defaultWebSecurityManager(
            @Qualifier("userRealm") UserRealm userRealm){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(userRealm);    //注入
        return securityManager;
    }

    @Bean(name="userRealm")
    public UserRealm userRealm(){
        UserRealm userRealm = new UserRealm();
        userRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return userRealm;
    }


}
