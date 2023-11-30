package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.Comment;
import com.example.entity.Likes;
import com.example.mapper.CommentMapper;
import com.example.mapper.LikesMapper;
import com.example.service.LikesService;
import com.example.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service("likesService")
public class LikesServiceImpl implements LikesService {

    @Resource
    private LikesMapper likesMapper;

    @Resource
    private CommentMapper commentMapper;

    @Override
    public Map<Long, Boolean> judgeVotedByCommentIdList(List<Comment> commentList, String token) throws Exception {
        Claims claims = JwtUtil.parseJWT(token);
        String userId = claims.getSubject();

        Map<Long, Boolean> res = new HashMap<>();
        commentList.forEach((elem) -> res.put(elem.getCommentId(), false));

        List<Likes> likesList = likesMapper.selectList(new LambdaQueryWrapper<Likes>().eq(Likes::getUserId, userId));
        likesList.forEach((elem) -> {
            if (res.containsKey(elem.getCommentId()))
                res.put(elem.getCommentId(), true);
        });
        return res;
    }

    @Override
    public List<Likes> findLikesByCurUser(String token) throws Exception {
        Claims claims = JwtUtil.parseJWT(token);
        String userId = claims.getSubject();

        return likesMapper.selectList(new LambdaQueryWrapper<Likes>()
                .eq(Likes::getUserId, userId)
        );
    }

    @Override
    public int voteCommentByCommentId(Long commentId, String token) throws Exception {

        Claims claims = JwtUtil.parseJWT(token);
        String userId = claims.getSubject();

        if (likesMapper.selectOne(new LambdaQueryWrapper<Likes>()
                .eq(Likes::getUserId, userId)
                .eq(Likes::getCommentId, commentId)) != null)
            throw new Exception("重复点赞");

        // 插入点赞记录到表中
        Likes like = new Likes(userId, commentId);

        return likesMapper.insert(like);
    }

    @Override
    public int cancelVoteByCommentId(Long id, String token) throws Exception {
        Claims claims = JwtUtil.parseJWT(token);
        String userId = claims.getSubject();

        return likesMapper.delete(new LambdaQueryWrapper<Likes>()
                .eq(Likes::getUserId, userId)
                .eq(Likes::getCommentId, id)
        );
    }

    @Override
    public IPage<Likes> findAllLikes(int pageNum, int pageSize) {
        return likesMapper.selectPage(new Page<>(pageNum, pageSize), new LambdaQueryWrapper<>());
    }

    @Override
    public IPage<Likes> findLikesById(String userId, Long commentId, Integer pageNum, Integer pageSize) {

        if (userId != null && !userId.equals("")) {
            return likesMapper.selectPage(new Page<>(pageNum, pageSize),
                    new LambdaQueryWrapper<Likes>().eq(Likes::getUserId, userId));
        } else if (commentId != null) {
            return likesMapper.selectPage(new Page<>(pageNum, pageSize),
                    new LambdaQueryWrapper<Likes>().eq(Likes::getCommentId, commentId));
        }

        return null;
    }
}