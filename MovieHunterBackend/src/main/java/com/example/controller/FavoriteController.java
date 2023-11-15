package com.example.controller;

import com.example.entity.ResponseResult;
import com.example.entity.Favorite;
import com.example.service.FavoriteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
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
    public ResponseResult insertFavorite(@RequestBody @Valid Favorite favorite) {
        //若当前用户已有相同名字的收藏夹
        List<Favorite> favoritesByUserId = favoriteService.findAllFavoritesByUserId(favorite.getUserId());

        int flag = 0;
        for (Favorite elem : favoritesByUserId) {
            if (elem.getName().equals(favorite.getName())) {
                flag = 1;
                break;
            }
        }

        if (flag == 1) {
            return new ResponseResult<>(500, "已存在相同名称的收藏夹，请重新命名");
        }

        int res = favoriteService.insertFavorite(favorite);
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
    public ResponseResult updateFavouriteById(@RequestBody @Valid Favorite favorite) {
        return new ResponseResult<>(200, "操作成功", favoriteService.updateFavouriteById(favorite));
    }

    @PreAuthorize(value = "hasAuthority('sys:post')")
    @ApiOperation("全量更新收藏夹， null值会更新")
    @PostMapping("/setFavouriteById")
    public ResponseResult setFavouriteById(@RequestBody @Valid Favorite favorite) {
        return new ResponseResult<>(200, "操作成功", favoriteService.setFavouriteById(favorite));
    }
}
