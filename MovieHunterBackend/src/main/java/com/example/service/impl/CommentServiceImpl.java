package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.Comment;
import com.example.entity.Movie;
import com.example.entity.User;
import com.example.mapper.CommentMapper;
import com.example.mapper.MovieMapper;
import com.example.service.CommentService;
import com.example.service.UserService;
import com.example.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Transactional
@Service("commentService")
public class CommentServiceImpl implements CommentService {

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private UserService userService;

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

    @Override
    public int deleteCommentById(Long id) {
        return commentMapper.deleteById(id);
    }

    @Override
    public int insertComment(Comment comment, String token) throws Exception {
        Claims claims = JwtUtil.parseJWT(token);
        String userId = claims.getSubject();
        comment.setUserId(userId);
        return commentMapper.insert(comment);
    }
}
