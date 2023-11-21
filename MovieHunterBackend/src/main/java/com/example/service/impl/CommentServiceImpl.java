package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.Comment;
import com.example.entity.Likes;
import com.example.entity.Movie;
import com.example.entity.User;
import com.example.mapper.CommentMapper;
import com.example.mapper.LikesMapper;
import com.example.mapper.MovieMapper;
import com.example.service.CommentService;
import com.example.service.UserService;
import com.example.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service("commentService")
public class CommentServiceImpl implements CommentService {

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private LikesMapper likesMapper;

    @Override
    public Comment findCommentById(Long id) {
        return commentMapper.selectOne(
                new LambdaQueryWrapper<Comment>()
                        .eq(Comment::getCommentId, id)
        );
    }

    @Override
    public IPage<Comment> findCommentsByMovieId(Long movieId, String token, Integer pageNum, Integer pageSize) throws Exception {
        IPage<Comment> page = commentMapper.selectPage(new Page<>(pageNum, pageSize),
                new LambdaQueryWrapper<Comment>()
                        .eq(Comment::getMovieId, movieId)
        );
        List<Comment> commentList = page.getRecords();

        Claims claims = JwtUtil.parseJWT(token);
        String curUserId = claims.getSubject();

        // 获取commentId列表
        List<Long> commentIdList = new ArrayList<>();
        commentList.forEach((elem) -> commentIdList.add(elem.getCommentId()));

        if (!commentIdList.isEmpty()) {
            // 判断当前用户是否已点赞
            List<Likes> likesList = likesMapper.selectList(new LambdaQueryWrapper<Likes>()
                    .eq(Likes::getUserId, curUserId)
                    .in(Likes::getCommentId, commentIdList)
            );
            List<Long> votedCommentIdList = new ArrayList<>();
            likesList.forEach((elem) -> votedCommentIdList.add(elem.getCommentId()));

            commentList.forEach((elem) -> {
                if (votedCommentIdList.contains(elem.getCommentId()))
                    elem.setIsVoted(true);
            });
        }
        return page;
    }

    @Override
    public IPage<Comment> findCommentsByUserId(String userId, String token, Integer pageNum, Integer pageSize) throws Exception {
        IPage<Comment> page = commentMapper.selectPage(new Page<>(pageNum, pageSize),
                new LambdaQueryWrapper<Comment>()
                        .eq(Comment::getUserId, userId)
        );
        List<Comment> commentList = page.getRecords();

        Claims claims = JwtUtil.parseJWT(token);
        String curUserId = claims.getSubject();

        // 获取commentId列表
        List<Long> commentIdList = new ArrayList<>();
        commentList.forEach((elem) -> commentIdList.add(elem.getCommentId()));

        if (!commentIdList.isEmpty()) {
            // 判断当前用户是否已点赞
            List<Likes> likesList = likesMapper.selectList(new LambdaQueryWrapper<Likes>()
                    .eq(Likes::getUserId, curUserId)
                    .in(Likes::getCommentId, commentIdList)
            );
            List<Long> votedCommentIdList = new ArrayList<>();
            likesList.forEach((elem) -> votedCommentIdList.add(elem.getCommentId()));

            commentList.forEach((elem) -> {
                if (votedCommentIdList.contains(elem.getCommentId()))
                    elem.setIsVoted(true);
            });
        }
        return page;
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
