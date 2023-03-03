package com.makima.blog.shiro;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

public class ShiroMD5 {
    public static Object MD5(String username ,String password){
        ByteSource salt = ByteSource.Util.bytes(username);
        return new SimpleHash("MD5",password,salt,1024);
    }

    public static void main(String[] args) {
        // 保存密码到数据库的时候，可以将手机号、密码输入进来，这将生成一个加密后的字符串，
        // 将生成的加密字符串保存至数据库即可，下次登陆时将使用加密串进行核验
        System.out.println(MD5("makima@163.com", "123456"));
    }

}
