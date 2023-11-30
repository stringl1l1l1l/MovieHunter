package com.example.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.entity.Comment;
import com.example.entity.Likes;
import com.example.entity.ResponseResult;
import com.example.service.CommentService;
import com.example.service.LikesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api
@Validated
@RestController
@RequestMapping("/likes")
public class LikesController {

    @Resource
    private LikesService likesService;

    @PreAuthorize(value = "hasAuthority('sys:post')")
    @ApiOperation("判断对于给定的评论id列表中当前登录用户是否点赞")
    @PostMapping("/judgeVotedByCommentIdList")
    public ResponseResult judgeVotedByCommentIdList(@RequestBody List<Comment> commentList, @RequestHeader String token) throws Exception {
        return new ResponseResult<>(200, "操作成功", likesService.judgeVotedByCommentIdList(commentList, token));
    }

    @PreAuthorize(value = "hasAuthority('sys:get')")
    @ApiOperation("查询当前登录用户的所有点赞")
    @GetMapping("/findLikesByCurUser")
    public ResponseResult findLikesByCurUser(@RequestHeader String token) throws Exception {
        return new ResponseResult<>(200, "操作成功", likesService.findLikesByCurUser(token));
    }

    @PreAuthorize(value = "hasAuthority('sys:get')")
    @ApiOperation("当前登录用户给指定ID的评论点赞")
    @GetMapping("/voteCommentById/{commentId}")
    public ResponseResult voteCommentById(@PathVariable Long commentId, @RequestHeader String token) throws Exception {
        int res = likesService.voteCommentByCommentId(commentId, token);
        Map<String, Integer> map = new HashMap<>();
        map.put("影响行数", res);
        return new ResponseResult<>(200, "操作成功", map);
    }

    @PreAuthorize(value = "hasAuthority('sys:delete')")
    @ApiOperation("当前登录用户给指定ID的评论取消点赞")
    @DeleteMapping("/cancelVoteByCommentId/{commentId}")
    public ResponseResult cancelVoteByCommentId(@PathVariable Long commentId, @RequestHeader String token) throws Exception {
        int res = likesService.cancelVoteByCommentId(commentId, token);
        Map<String, Integer> map = new HashMap<>();
        map.put("影响行数", res);
        return new ResponseResult<>(200, "操作成功", map);
    }

    @PreAuthorize(value = "hasAuthority('sys:manager')")
    @ApiOperation("查询所有点赞")
    @GetMapping("/findAllLikes")
    public ResponseResult findAllLikes(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "7") Integer pageSize) throws Exception {
        return new ResponseResult<>(200, "操作成功", likesService.findAllLikes(pageNum, pageSize));
    }

    @PreAuthorize(value = "hasAuthority('sys:manager')")
    @ApiOperation("根据评论或用户ID查询点赞")
    @GetMapping("/findLikesById")
    public ResponseResult findAllLikes(@RequestParam(required = false) String userId, @RequestParam(required = false) Long commentId, @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "7") Integer pageSize) throws Exception {
        return new ResponseResult<>(200, "操作成功", likesService.findLikesById(userId, commentId, pageNum, pageSize));
    }
}
