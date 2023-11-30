package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.entity.Comment;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CommentService {

    IPage<Comment> findAllComments(Integer pageNum, Integer pageSize);

    Comment findCommentById(Long id);

    IPage<Comment> findCommentsByMovieId(Long movieId, String token, Integer pageNum, Integer pageSize) throws Exception;

    IPage<Comment> findCommentsByUserId(String userId, String token, Integer pageNum, Integer pageSize) throws Exception;

    int deleteCommentById(Long id, String token) throws Exception;

    int insertComment(Comment comment, String token) throws Exception;

    int deleteAnyCommentById(Long id);

    IPage<Comment> findVotedCommentsByCurUser(String token, Integer pageNum, Integer pageSize) throws Exception;
}
