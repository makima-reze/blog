package com.makima.blog.handler;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.makima.blog.util.PageUtils;
import com.mysql.cj.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static com.makima.blog.constant.CommonConst.CURRENT;
import static com.makima.blog.constant.CommonConst.DEFAULT_SIZE;
import static com.makima.blog.constant.CommonConst.SIZE;


/**
 * 分页拦截器
 *
 * @author yezhiqiu
 * @date 2021/07/18
 **/
public class PageableHandlerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String currentPage = request.getParameter(CURRENT);
        String pageSize = Optional.ofNullable(request.getParameter(SIZE)).orElse(DEFAULT_SIZE);
        if (!StringUtils.isNullOrEmpty(currentPage)) {
            PageUtils.setCurrentPage(new Page<>(Long.parseLong(currentPage), Long.parseLong(pageSize)));
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        PageUtils.remove();
    }

}