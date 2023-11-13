package com.example.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.entity.Movie;
import com.example.entity.ResponseResult;
import com.example.entity.User;
import com.example.service.MovieService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Api
@Validated
@RestController
@RequestMapping("/movie")
public class MovieController {

    @Resource
    private MovieService movieService;

    private static final Logger logger = LoggerFactory.getLogger(MovieController.class);

    @PreAuthorize(value = "hasAuthority('sys:get')")
    @ApiOperation("根据ID查询电影")
    @GetMapping("/findMovieById/{id}")
    public ResponseResult findMovieById(@PathVariable Long id) {
        Movie movie = movieService.findMovieById(id);
        if (Objects.isNull(movie)) {
            return new ResponseResult<>(200, "ID不存在");
        } else
            return new ResponseResult<>(200, "操作成功", movie);
    }

    @PreAuthorize(value = "hasAuthority('sys:get')")
    @ApiOperation("分页查询电影")
    @GetMapping("/findMoviesByPages/{pageNum}")
    public ResponseResult findMoviesByPages(@PathVariable(required = false) Integer pageNum, @RequestParam(defaultValue = "7") Integer pageSize) {
        if (pageNum == null)
            pageNum = 1;
        IPage<Movie> page = movieService.findMoviesByPages(pageNum, pageSize);
        return new ResponseResult<>(200, "操作成功", page);
    }

    @PreAuthorize(value = "hasAuthority('sys:get')")
    @ApiOperation("根据电影类别查询电影")
    @GetMapping("/findMoviesByPages/{mask}")
    public ResponseResult findMoviesByGenresMask(@PathVariable Integer mask) {
        List<Movie> movies = movieService.findMoviesByGenresMask(mask);
        return new ResponseResult<>(200, "操作成功", movies);
    }
}
