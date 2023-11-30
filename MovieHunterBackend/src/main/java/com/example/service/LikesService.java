package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.entity.Comment;
import com.example.entity.Likes;

import java.util.List;
import java.util.Map;

public interface LikesService {

    Map<Long, Boolean> judgeVotedByCommentIdList(List<Comment> commentList, String token) throws Exception;

    List<Likes> findLikesByCurUser(String token) throws Exception;

    int voteCommentByCommentId(Long id, String token) throws Exception;

    int cancelVoteByCommentId(Long id, String token) throws Exception;

    IPage<Likes> findAllLikes(int pageNum, int pageSize);

    IPage<Likes> findLikesById(String userId, Long commentId, Integer pageNum, Integer pageSize);
}
