package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.entity.Comment;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CommentService {

    Comment findCommentById(Long id);

    IPage<Comment> findCommentsByMovieId(Long movieId, String token, Integer pageNum, Integer pageSize) throws Exception;

    IPage<Comment> findCommentsByUserId(String userId, String token, Integer pageNum, Integer pageSize) throws Exception;

    int deleteCommentById(Long id);

    int insertComment(Comment comment, String token) throws Exception;
}
