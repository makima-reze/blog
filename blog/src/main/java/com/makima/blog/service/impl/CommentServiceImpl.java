package com.makima.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.makima.blog.dao.CommentDao;
import com.makima.blog.dto.CommentDTO;
import com.makima.blog.dto.ReplyCountDTO;
import com.makima.blog.dto.ReplyDTO;
import com.makima.blog.entity.Comment;
import com.makima.blog.entity.UserAuth;
import com.makima.blog.service.CommentService;
import com.makima.blog.util.HTMLUtils;
import com.makima.blog.util.PageUtils;
import com.makima.blog.vo.CommentVO;
import com.makima.blog.vo.PageResult;
import com.makima.blog.vo.WebsiteConfigVO;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.makima.blog.constant.CommonConst.TRUE;
import static com.makima.blog.constant.RedisPrefixConst.COMMENT_LIKE_COUNT;

/**
 * @author dai17
 * @create 2022-12-28 13:49
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentDao,Comment> implements CommentService {

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public PageResult<CommentDTO> listComments(CommentVO commentVO) {
        // 查询评论量
        Integer commentCount = commentDao.selectCount(new QueryWrapper<Comment>()
                .eq(Objects.nonNull(commentVO.getTopicId()), "topic_id", commentVO.getTopicId())
                .eq("type", commentVO.getType())
                .isNull("parent_id")
                .eq("is_review", TRUE));
        if (commentCount == 0) {
            return new PageResult<>();
        }
        // 分页查询评论数据
        List<CommentDTO> commentDTOList = commentDao.listComments(PageUtils.getLimitCurrent(), PageUtils.getSize(), commentVO);
        if (CollectionUtils.isEmpty(commentDTOList)) {
            return new PageResult<>();
        }
        // 查询redis的评论点赞数据
        Map<String,Object> likeCountMap = redisTemplate.boundHashOps(COMMENT_LIKE_COUNT).entries();
        // 提取评论id集合
        List<Integer> commentIdList = commentDTOList.stream()
                .map(CommentDTO::getId)
                .collect(Collectors.toList());
        // 根据评论id集合查询回复数据
        List<ReplyDTO> replyDTOList = commentDao.listReplies(commentIdList);
        // 封装回复点赞量
        replyDTOList.forEach(item -> item.setLikeCount((Integer) likeCountMap.get(item.getId().toString())));
        // 根据评论id分组回复数据
        Map<Integer, List<ReplyDTO>> replyMap = replyDTOList.stream()
                .collect(Collectors.groupingBy(ReplyDTO::getParentId));
        // 根据评论id查询回复量
        Map<Integer, Integer> replyCountMap = commentDao.listReplyCountByCommentId(commentIdList)
                .stream().collect(Collectors.toMap(ReplyCountDTO::getCommentId, ReplyCountDTO::getReplyCount));
        // 封装评论数据
        commentDTOList.forEach(item -> {
            item.setLikeCount((Integer) likeCountMap.get(item.getId().toString()));
            item.setReplyDTOList(replyMap.get(item.getId()));
            item.setReplyCount(replyCountMap.get(item.getId()));
        });
        return new PageResult<>(commentDTOList, commentCount);
    }

    @Override
    public void saveComment(@Valid CommentVO commentVO) {
        commentVO.setCommentContent(HTMLUtils.filter(commentVO.getCommentContent()));
        UserAuth usersEntity = (UserAuth) SecurityUtils.getSubject().getPrincipal();
        Comment comment = Comment.builder()
                .userId(usersEntity.getUserInfoId())
                .replyUserId(commentVO.getReplyUserId())
                .topicId(commentVO.getTopicId())
                .commentContent(commentVO.getCommentContent())
                .parentId(commentVO.getParentId())
                .type(commentVO.getType())
                .isReview(TRUE)
                .createTime(LocalDateTime.now())
                .build();
        commentDao.insert(comment);
    }
}
