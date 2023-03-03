package com.makima.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.makima.blog.dao.PageDao;
import com.makima.blog.entity.Page;
import com.makima.blog.service.PageService;
import com.makima.blog.util.BeanCopyUtils;
import com.makima.blog.vo.PageVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

import static com.makima.blog.constant.RedisPrefixConst.PAGE_COVER;

/**
 * @author dai17
 * @create 2022-12-18 14:57
 */
@Service
public class pageServiceImpl extends ServiceImpl<PageDao,Page> implements PageService {

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private PageDao pageDao;

    @Override
    public List<PageVO> listPages() {
        List<PageVO> pageVOList;
        Object pageList = redisTemplate.opsForValue().get(PAGE_COVER);
        if (Objects.nonNull(pageList)){
            pageVOList = JSON.parseObject(pageList.toString(),List.class);
        }else {
            pageVOList = BeanCopyUtils.copyList(pageDao.selectList(null),PageVO.class);
            redisTemplate.opsForValue().set(PAGE_COVER,JSON.toJSONString(pageVOList));
        }
        return pageVOList;
    }
}
