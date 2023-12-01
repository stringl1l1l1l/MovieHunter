package com.example.controller;

import com.example.entity.FavoriteMovie;
import com.example.entity.Movie;
import com.example.entity.ResponseResult;
import com.example.entity.Favorite;
import com.example.service.FavoriteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ResponseHeader;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api
@Validated
@RestController
@RequestMapping("/favorite")
public class FavoriteController {

    @Resource
    private FavoriteService favoriteService;

    @PreAuthorize(value = "hasAuthority('sys:put')")
    @ApiOperation("创建收藏夹")
    @PutMapping("/insertFavorite")
    public ResponseResult insertFavorite(@RequestBody @Valid Favorite favorite, @RequestHeader String token) throws Exception {
        int res = favoriteService.insertFavorite(favorite, token);
        Map<String, Integer> map = new HashMap<>();
        map.put("影响行数", res);

        return new ResponseResult<>(200, "操作成功", map);
    }


    @PreAuthorize(value = "hasAuthority('sys:get')")
    @ApiOperation("根据用户id查询所有收藏夹")
    @GetMapping("/insertFavorite/{userId}")
    public ResponseResult findAllFavoritesByUserId(@PathVariable String userId) {
        return new ResponseResult<>(200, "操作成功", favoriteService.findAllFavoritesByUserId(userId));
    }

    @PreAuthorize(value = "hasAuthority('sys:post')")
    @ApiOperation("增量更新收藏夹， null值不会更新")
    @PostMapping("/updateFavouriteById")
    public ResponseResult updateFavouriteById(@RequestBody @Valid Favorite favorite, @RequestHeader String token) throws Exception {
        int res = favoriteService.updateFavouriteById(favorite,token);
        Map<String, Integer> map = new HashMap<>();
        map.put("影响行数", res);
        return new ResponseResult<>(200, "操作成功", map);
    }

    @PreAuthorize(value = "hasAuthority('sys:post')")
    @ApiOperation("全量更新收藏夹， null值会更新")
    @PostMapping("/setFavouriteById")
    public ResponseResult setFavouriteById(@RequestBody @Valid Favorite favorite, @RequestHeader String token) throws Exception {
        int res = favoriteService.setFavouriteById(favorite,token);
        Map<String, Integer> map = new HashMap<>();
        map.put("影响行数", res);
        return new ResponseResult<>(200, "操作成功", map);
    }

    @PreAuthorize(value = "hasAuthority('sys:get')")
    @ApiOperation("添加指定movie到指定收藏夹")
    @PutMapping("/insertMovieToFavorite")
    public ResponseResult insertMovieToFavorite(@RequestBody @Valid FavoriteMovie favoriteMovie) throws Exception {
        int res = favoriteService.insertMovieToFavorite(favoriteMovie);
        Map<String, Integer> map = new HashMap<>();
        map.put("影响行数", res);
        return new ResponseResult<>(200, "操作成功", map);
    }

    @PreAuthorize(value = "hasAuthority('sys:delete')")
    @ApiOperation("删除指定收藏夹中的指定电影")
    @DeleteMapping("/deleteMovieInFavorite")
    public ResponseResult deleteMovieInFavorite(@RequestBody @Valid FavoriteMovie favoriteMovie) {
        int res = favoriteService.deleteMovieInFavorite(favoriteMovie);
        Map<String, Integer> map = new HashMap<>();
        map.put("影响行数", res);
        return new ResponseResult<>(200, "操作成功", map);
    }

    @PreAuthorize(value = "hasAuthority('sys:get')")
    @ApiOperation("查询当前用户的所有收藏夹")
    @GetMapping("/findFavoritesByCurUser")
    public ResponseResult findFavoritesByCurUser(@RequestHeader String token) throws Exception {
        return new ResponseResult<>(200, "操作成功", favoriteService.findFavoritesByCurUser(token));
    }

    @PreAuthorize(value = "hasAuthority('sys:get')")
    @ApiOperation("查询指定收藏夹的所有电影")
    @GetMapping("/findAllMoviesInFavorite/{favoriteId}")
    public ResponseResult findAllMoviesInFavorite(@PathVariable Long favoriteId) {
        return new ResponseResult<>(200, "操作成功", favoriteService.findAllMoviesInFavorite(favoriteId));
    }

    @PreAuthorize(value = "hasAuthority('sys:delete')")
    @ApiOperation("删除指定收藏夹")
    @DeleteMapping("/deleteFavoriteById/{favoriteId}")
    public ResponseResult deleteFavoriteById(@PathVariable Long favoriteId) {
        int res = favoriteService.deleteFavoriteById(favoriteId);
        Map<String, Integer> map = new HashMap<>();
        map.put("影响行数", res);
        return new ResponseResult<>(200, "操作成功", map);
    }

    @PreAuthorize(value = "hasAuthority('sys:post')")
    @ApiOperation("移动电影到指定收藏夹")
    @PostMapping("/moveMovieToFavorite/{newFavoriteId}")
    public ResponseResult moveMovieToFavorite(@RequestBody FavoriteMovie favoriteMovie, @PathVariable Long newFavoriteId, @RequestHeader String token) throws Exception {
        int res = favoriteService.moveMovieToFavorite(favoriteMovie, newFavoriteId, token);
        Map<String, Integer> map = new HashMap<>();
        map.put("影响行数", res);
        return new ResponseResult<>(200, "操作成功", map);
    }
}
