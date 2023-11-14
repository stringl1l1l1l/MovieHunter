package com.example.controller;

import com.example.entity.ResponseResult;
import com.example.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api
@Validated
@RestController("/comment")
public class CommentController {

    @Resource
    private CommentService commentService;

    @PreAuthorize(value = "hasAuthority('sys:get')")
    @ApiOperation("根据电影ID找到相关评论")
    @GetMapping("/findCommentsByMovieId/{movieId}")
    public ResponseResult findCommentsByMovieId(@PathVariable Long movieId, @RequestParam(defaultValue = "1")Integer pageNum, @RequestParam(defaultValue = "7")Integer pageSize) {
        return new ResponseResult<>(200, "操作成功", commentService.findCommentsByMovieId(movieId, pageNum, pageSize));
    }

    @PreAuthorize(value = "hasAuthority('sys:get')")
    @ApiOperation("根据用户ID找到相关评论")
    @GetMapping("/findCommentsByUserId/{userId}")
    public ResponseResult findCommentsByUserId(@PathVariable String userId, @RequestParam(defaultValue = "1")Integer pageNum, @RequestParam(defaultValue = "7")Integer pageSize) {
        return new ResponseResult<>(200, "操作成功", commentService.findCommentsByUserId(userId, pageNum, pageSize));
    }
}
