package com.makima.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.makima.blog.dao.CommentDao;
import com.makima.blog.dao.TalkDao;
import com.makima.blog.dto.CommentCountDTO;
import com.makima.blog.dto.TalkDTO;
import com.makima.blog.entity.Talk;
import com.makima.blog.service.TalkService;
import com.makima.blog.util.CommonUtils;
import com.makima.blog.util.HTMLUtils;
import com.makima.blog.util.PageUtils;
import com.makima.blog.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.makima.blog.constant.ArticleStatusEnum.PUBLIC;
import static com.makima.blog.constant.RedisPrefixConst.TALK_LIKE_COUNT;

/**
 * @author dai17
 * @create 2022-12-19 11:52
 */
@Service
public class talkServiceImpl extends ServiceImpl<TalkDao,Talk> implements TalkService {

    @Resource
    private TalkDao talkDao;

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public List<String> listHomeTalks() {
        //获取主页说说内容
        return talkDao.selectList(new QueryWrapper<Talk>()
                .eq("status",PUBLIC.getStatus())
                .orderByDesc("is_top")
                .orderByDesc("id")
                .last("limit 10"))
                .stream()
                .map(item -> item.getContent().length() > 200 ? HTMLUtils.deleteHMTLTag(item.getContent().substring(0, 200)) : HTMLUtils.deleteHMTLTag(item.getContent()))
                .collect(Collectors.toList());
    }

    @Override
    public PageResult<TalkDTO> listTalks() {
        Integer count = talkDao.selectCount(new QueryWrapper<Talk>()
                .eq("status", PUBLIC.getStatus()));
        if (count==0){
            return new PageResult<>();
        }
        List<TalkDTO> talkDTOList = talkDao.listTalks(PageUtils.getLimitCurrent(), PageUtils.getSize());
        // 查询说说评论量
        List<Integer> talkIdList = talkDTOList.stream()
                .map(TalkDTO::getId)
                .collect(Collectors.toList());
        Map<Integer, Integer> commentCountMap = commentDao.listCommentCountByTopicIds(talkIdList)
                .stream()
                .collect(Collectors.toMap(CommentCountDTO::getId, CommentCountDTO::getCommentCount));
        // 查询说说点赞量
        Map<String, Object> likeCountMap = redisTemplate.opsForHash().entries(TALK_LIKE_COUNT);
        talkDTOList.forEach(item -> {
            item.setLikeCount((Integer) likeCountMap.get(item.getId().toString()));
            item.setCommentCount(commentCountMap.get(item.getId()));
            // 转换图片格式
            if (Objects.nonNull(item.getImages())) {
                item.setImgList(CommonUtils.castList(JSON.parseObject(item.getImages(), List.class), String.class));
            }
        });
        return new PageResult<>(talkDTOList, count);
    }
}
