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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    @ApiOperation("分页查询所有电影")
    @GetMapping("/findAllMovies")
    public ResponseResult findAllMovies(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "7") Integer pageSize) {
        IPage<Movie> movie = movieService.findAllMovies(pageNum, pageSize);
        return new ResponseResult<>(200, "操作成功", movie);
    }

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
    @GetMapping("/findMoviesByGenresMask/{mask}")
    public ResponseResult findMoviesByGenresMask(@PathVariable Integer mask, @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "7") Integer pageSize) {
        IPage<Movie> movies = movieService.findMoviesByGenresMask(mask, pageNum, pageSize);
        return new ResponseResult<>(200, "操作成功", movies);
    }

    @PreAuthorize(value = "hasAuthority('sys:get')")
    @ApiOperation("根据上映地区查询电影")
    @GetMapping("/findMoviesByRegionsMask/{mask}")
    public ResponseResult findMoviesByRegionsMask(@PathVariable Integer mask, @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "7") Integer pageSize) {
        IPage<Movie> movies = movieService.findMoviesByRegionsMask(mask, pageNum, pageSize);
        return new ResponseResult<>(200, "操作成功", movies);
    }

    @PreAuthorize(value = "hasAuthority('sys:get')")
    @ApiOperation("模糊查询电影")
    @GetMapping("/findMoviesByName")
    public ResponseResult findMoviesByName(@RequestParam String name, @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "7") Integer pageSize) {
        return new ResponseResult<>(200, "操作成功", movieService.findMoviesByName(name, pageNum, pageSize));
    }

    @PreAuthorize(value = "hasAuthority('sys:manager')")
    @ApiOperation("删除电影")
    @DeleteMapping("/deleteMovieById/{movieId}")
    public ResponseResult deleteMovieById(@PathVariable Long movieId) {
        int res = movieService.deleteMovieById(movieId);
        Map<String, Integer> map = new HashMap<>();
        map.put("影响行数", res);
        return new ResponseResult<>(200, "操作成功", map);
    }

    @PreAuthorize(value = "hasAuthority('sys:manager')")
    @ApiOperation("全量更新电影")
    @PostMapping("/updateMovieById")
    public ResponseResult updateMovieById(@RequestBody Movie movie) {
        int res = movieService.updateMovieById(movie);
        Map<String, Integer> map = new HashMap<>();
        map.put("影响行数", res);
        return new ResponseResult<>(200, "操作成功", map);
    }

    @PreAuthorize(value = "hasAuthority('sys:manager')")
    @ApiOperation("增量更新电影")
    @PostMapping("/setMovieById")
    public ResponseResult setMovieById(@RequestBody Movie movie) {
        int res = movieService.setMovieById(movie);
        Map<String, Integer> map = new HashMap<>();
        map.put("影响行数", res);
        return new ResponseResult<>(200, "操作成功", map);
    }

    @PreAuthorize(value = "hasAuthority('sys:manager')")
    @ApiOperation("添加电影")
    @PutMapping("/insertMovie")
    public ResponseResult insertMovie(@RequestBody Movie movie) {
        int res = movieService.insertMovie(movie);
        Map<String, Integer> map = new HashMap<>();
        map.put("影响行数", res);
        return new ResponseResult<>(200, "操作成功", map);
    }
}
