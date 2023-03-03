package com.makima.blog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.makima.blog.dto.CommentCountDTO;
import com.makima.blog.dto.CommentDTO;
import com.makima.blog.dto.ReplyCountDTO;
import com.makima.blog.dto.ReplyDTO;
import com.makima.blog.entity.Comment;
import com.makima.blog.vo.CommentVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

/**
 * @author dai17
 * @create 2022-12-28 13:50
 */
@Repository
public interface CommentDao extends BaseMapper<Comment> {

    List<CommentDTO> listComments(@Param("current") Long limitCurrent, @Param("size") Long size, @Param("commentVO") CommentVO commentVO);

    List<ReplyDTO> listReplies(@Param("commentIdList") List<Integer> commentIdList);

    List<ReplyCountDTO> listReplyCountByCommentId(@Param("commentIdList") List<Integer> commentIdList);

    List<CommentCountDTO> listCommentCountByTopicIds(List<Integer> topicIdList);

}
