package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.entity.Comment;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CommentService {

    Comment findCommentById(Long id);

    IPage<Comment> findCommentsByMovieId(Long movieId, Integer pageNum, Integer pageSize);

    IPage<Comment> findCommentsByUserId(String userId, Integer pageNum, Integer pageSize);

    int deleteCommentById(Long id);
}
