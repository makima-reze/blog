package com.makima.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.makima.blog.entity.Page;
import com.makima.blog.vo.PageVO;

import java.util.List;

/**
 * @author dai17
 * @create 2022-12-18 14:57
 */

public interface PageService extends IService<Page>{


    List<PageVO> listPages();

}
