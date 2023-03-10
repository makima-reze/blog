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
            // ??????????????????????????????
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            String OriginalFilename = file.getOriginalFilename();//??????????????????
            String suffixName = OriginalFilename.substring(OriginalFilename.lastIndexOf("."));//?????????????????????
            //????????????????????????
            String filename = UUID.randomUUID().toString() +suffixName;
            File localFile = new File(uploadPath+"/"+filename);
            try {
                file.transferTo(localFile); //?????????????????????????????????
                /**
                 * ???????????????filename??????????????????,????????????????????????
                 */
                return "http://114.132.77.182:8080/img/"+filename;//??????????????????????????????????????????
            }catch (IOException e){
                e.printStackTrace();
                System.out.println("????????????");
                return "";
            }
        }else{
            System.out.println("????????????");
            return "";
        }

    }
}
