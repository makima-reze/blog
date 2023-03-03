package com.makima.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.makima.blog.dao.UserInfoDao;
import com.makima.blog.entity.UserAuth;
import com.makima.blog.entity.UserInfo;
import com.makima.blog.service.UserInfoService;
import com.makima.blog.vo.Result;
import com.makima.blog.vo.UserInfoVO;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @author dai17
 * @create 2022-12-24 16:42
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoDao,UserInfo> implements UserInfoService {

    @Resource
    private UserInfoDao userInfoDao;

    @Transactional
    @Override
    public void updateUserInfo(@Valid UserInfoVO userInfoVO) {
        UserAuth usersEntity = (UserAuth) SecurityUtils.getSubject().getPrincipal();
        UserInfo userInfo = UserInfo.builder().id(usersEntity.getUserInfoId())
                .nickname(userInfoVO.getNickname())
                .intro(userInfoVO.getIntro())
                .webSite(userInfoVO.getWebSite())
                .build();
        userInfoDao.updateById(userInfo);
    }

    @Override
    public String updateUserAvatar(MultipartFile file) throws IOException {
        String result = upload(file);
        UserAuth usersEntity = (UserAuth) SecurityUtils.getSubject().getPrincipal();
        UserInfo userInfo = UserInfo.builder()
                .id(usersEntity.getUserInfoId())
                .avatar(result)
                .build();
        userInfoDao.updateById(userInfo);
        return result;
    }

    private String upload(MultipartFile file){
        if(!file.isEmpty()){
//            String uploadPath = "C:\\uploadFile";
            String uploadPath = "/usr/local/upload";
            // 如果目录不存在则创建
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            String OriginalFilename = file.getOriginalFilename();//获取原文件名
            String suffixName = OriginalFilename.substring(OriginalFilename.lastIndexOf("."));//获取文件后缀名
            //重新随机生成名字
            String filename = UUID.randomUUID().toString() +suffixName;
            File localFile = new File(uploadPath+"/"+filename);
            try {
                file.transferTo(localFile); //把上传的文件保存至本地
                /**
                 * 这里应该把filename保存到数据库,供前端访问时使用
                 */
                return "http://114.132.77.182:8080/img/"+filename;//上传成功，返回保存的文件地址
            }catch (IOException e){
                e.printStackTrace();
                System.out.println("上传失败");
                return "";
            }
        }else{
            System.out.println("文件为空");
            return "";
        }

    }
}
