package com.makima.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.makima.blog.dto.BlogHomeInfoDTO;
import com.makima.blog.vo.WebsiteConfigVO;


public interface BlogInfoService {


    BlogHomeInfoDTO getBlogHomeInfo();

    WebsiteConfigVO getWebsiteConfig();

}
