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
import com.example.service.LikesService;
import com.example.service.UserService;
import com.example.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Transactional
@Service("commentService")
public class CommentServiceImpl implements CommentService {

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private LikesMapper likesMapper;

    @Resource
    private LikesService likesService;

    @Override
    public IPage<Comment> findAllComments(Integer pageNum, Integer pageSize) {
        return commentMapper.selectPage(new Page<>(pageNum, pageSize), new LambdaQueryWrapper<>());
    }

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

        // 设置当前评论是否属于当前用户的字段
        commentList.forEach(elem -> {
            if (Objects.equals(elem.getUserId(), curUserId)) {
                elem.setIsOwned(true);
            }
        });

        // 获取commentId列表
        List<Long> commentIdList = new ArrayList<>();
        commentList.forEach((elem) -> commentIdList.add(elem.getCommentId()));

        if (!commentIdList.isEmpty()) {
            // 查出当前用户对当前评论的点赞情况
            List<Likes> likesList = likesMapper.selectList(new LambdaQueryWrapper<Likes>()
                    .eq(Likes::getUserId, curUserId)
                    .in(Likes::getCommentId, commentIdList)
            );
            List<Long> votedCommentIdList = new ArrayList<>();
            likesList.forEach((elem) -> votedCommentIdList.add(elem.getCommentId()));

            // 设置当前用户是否已点赞的字段
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

        // 设置当前评论是否属于当前用户的字段
        if (Objects.equals(userId, curUserId)) {
            commentList.forEach(elem -> {
                elem.setIsOwned(true);
            });
        }

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
    public int deleteCommentById(Long id, String token) throws Exception {
        Claims claims = JwtUtil.parseJWT(token);
        String userId = claims.getSubject();
        if (!Objects.equals(commentMapper.selectById(id).getUserId(), userId))
            throw new Exception("用户只能删除自己的评论");
        return commentMapper.deleteById(id);
    }

    @Override
    public int insertComment(Comment comment, String token) throws Exception {
        Claims claims = JwtUtil.parseJWT(token);
        String userId = claims.getSubject();
        comment.setUserId(userId);
        return commentMapper.insert(comment);
    }

    @Override
    public int deleteAnyCommentById(Long id) {
        return commentMapper.deleteById(id);
    }

    @Override
    public IPage<Comment> findVotedCommentsByCurUser(String token, Integer pageNum, Integer pageSize) throws Exception {
        List<Likes> likesByCurUser = likesService.findLikesByCurUser(token);
        List<Long> commentIdList = new ArrayList<>();
        likesByCurUser.forEach(likes -> commentIdList.add(likes.getCommentId()));
        if (commentIdList.isEmpty())
            throw new Exception("当前用户没有点赞记录");

        IPage<Comment> res = commentMapper.selectPage(new Page<>(pageNum, pageSize),
                new LambdaQueryWrapper<Comment>().in(Comment::getCommentId, commentIdList));

        for (Comment record : res.getRecords()) {
            record.setIsVoted(true);
            record.setIsOwned(true);
        }

        return res;
    }
}
