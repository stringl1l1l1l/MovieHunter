package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.Comment;
import com.example.entity.Movie;
import com.example.mapper.CommentMapper;
import com.example.mapper.MovieMapper;
import com.example.service.CommentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Transactional
@Service("commentService")
public class CommentServiceImpl implements CommentService {

    @Resource
    private CommentMapper commentMapper;

    @Override
    public Comment findCommentById(Long id) {
        return commentMapper.selectOne(
                new LambdaQueryWrapper<Comment>()
                        .eq(Comment::getCommentId, id)
        );
    }

    @Override
    public IPage<Comment> findCommentsByMovieId(Long movieId, Integer pageNum, Integer pageSize) {
            return commentMapper.selectPage(new Page<>(pageNum, pageSize),
                    new LambdaQueryWrapper<Comment>()
                            .eq(Comment::getMovieId, movieId)
            );
    }

    @Override
    public IPage<Comment> findCommentsByUserId(String userId, Integer pageNum, Integer pageSize) {
        return commentMapper.selectPage(new Page<>(pageNum, pageSize),
                new LambdaQueryWrapper<Comment>()
                        .eq(Comment::getUserId, userId)
        );
    }
}
