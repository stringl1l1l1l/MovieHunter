package com.example.controller;

import com.example.entity.Comment;
import com.example.entity.ResponseResult;
import com.example.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Api
@Validated
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Resource
    private CommentService commentService;

    @PreAuthorize(value = "hasAuthority('sys:get')")
    @ApiOperation("根据电影ID找到相关评论")
    @GetMapping("/findCommentsByMovieId/{movieId}")
    public ResponseResult findCommentsByMovieId(@PathVariable Long movieId, @RequestHeader String token, @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "7") Integer pageSize) throws Exception {
        return new ResponseResult<>(200, "操作成功", commentService.findCommentsByMovieId(movieId, token, pageNum, pageSize));
    }

    @PreAuthorize(value = "hasAuthority('sys:get')")
    @ApiOperation("根据用户ID找到相关评论")
    @GetMapping("/findCommentsByUserId/{userId}")
    public ResponseResult findCommentsByUserId(@PathVariable String userId, @RequestHeader String token, @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "7") Integer pageSize) throws Exception {
        return new ResponseResult<>(200, "操作成功", commentService.findCommentsByUserId(userId, token, pageNum, pageSize));
    }

    @PreAuthorize(value = "hasAuthority('sys:delete')")
    @ApiOperation("删除当前用户指定ID的评论")
    @DeleteMapping("/deleteCommentById/{id}")
    public ResponseResult deleteCommentById(@PathVariable Long id, @RequestHeader String token) throws Exception {
        int res = commentService.deleteCommentById(id, token);
        Map<String, Integer> map = new HashMap<>();
        map.put("影响行数", res);
        return new ResponseResult<>(200, "操作成功", map);
    }

    @PreAuthorize(value = "hasAuthority('sys:put')")
    @ApiOperation("当前登录用户发表评论")
    @PutMapping("/insertComment")
    public ResponseResult insertComment(@RequestBody @Valid Comment comment, @RequestHeader String token) throws Exception {
        int res = commentService.insertComment(comment, token);
        Map<String, Integer> map = new HashMap<>();
        map.put("影响行数", res);
        return new ResponseResult<>(200, "操作成功", map);
    }

    @PreAuthorize(value = "hasAuthority('sys:manager')")
    @ApiOperation("查询所有评论")
    @GetMapping("/findAllComments")
    public ResponseResult findAllComments(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "7") Integer pageSize) throws Exception {
        return new ResponseResult<>(200, "操作成功", commentService.findAllComments(pageNum, pageSize));
    }

    @PreAuthorize(value = "hasAuthority('sys:manager')")
    @ApiOperation("根据ID找到评论")
    @GetMapping("/findCommentById/{commentId}")
    public ResponseResult findCommentById(@PathVariable Long commentId) throws Exception {
        return new ResponseResult<>(200, "操作成功", commentService.findCommentById(commentId));
    }

    @PreAuthorize(value = "hasAuthority('sys:manager')")
    @ApiOperation("删除任意指定ID的评论")
    @DeleteMapping("/deleteAnyCommentById/{id}")
    public ResponseResult deleteAnyCommentById(@PathVariable Long id) {
        int res = commentService.deleteAnyCommentById(id);
        Map<String, Integer> map = new HashMap<>();
        map.put("影响行数", res);
        return new ResponseResult<>(200, "操作成功", map);
    }

    @PreAuthorize(value = "hasAuthority('sys:get')")
    @ApiOperation("找到当前用户点赞的评论")
    @GetMapping("/findVotedCommentsByCurUser")
    public ResponseResult findVotedCommentsByCurUser( @RequestHeader String token, @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "7") Integer pageSize) throws Exception {
        return new ResponseResult<>(200, "操作成功", commentService.findVotedCommentsByCurUser(token, pageNum, pageSize));
    }
}
