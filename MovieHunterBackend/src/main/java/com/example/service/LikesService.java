package com.example.service;

import com.example.entity.Likes;

import java.util.List;
import java.util.Map;

public interface LikesService {

    Map<Long, Boolean> judgeVotedByCommentIdList(List<Long> commentIdList, String token) throws Exception;

    List<Likes> findLikesByCurUser(String token) throws Exception;

    int voteCommentByCommentId(Long id, String token) throws Exception;

    int cancelVoteByCommentId(Long id, String token) throws Exception;
}
