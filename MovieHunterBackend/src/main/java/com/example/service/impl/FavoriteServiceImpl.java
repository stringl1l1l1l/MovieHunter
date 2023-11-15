package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.entity.Favorite;
import com.example.entity.User;
import com.example.mapper.FavoriteMapper;
import com.example.mapper.MovieMapper;
import com.example.service.FavoriteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


@Transactional
@Service("favoriteService")
public class FavoriteServiceImpl implements FavoriteService {

    @Resource
    private FavoriteMapper favoriteMapper;

    @Override
    public List<Favorite> findAllFavoritesByUserId(String userId) {
        return favoriteMapper.selectList(
                new LambdaQueryWrapper<Favorite>()
                        .eq(Favorite::getUserId, userId)
        );
    }

    @Override
    public int updateFavouriteById(Favorite favorite) {
        return favoriteMapper.update(favorite,
                new LambdaQueryWrapper<Favorite>()
                        .eq(Favorite::getFavoriteId, favorite.getFavoriteId())
        );
    }

    @Override
    public int setFavouriteById(Favorite favorite) {
        LambdaUpdateWrapper<Favorite> wrapper = new LambdaUpdateWrapper<>();

        wrapper.eq(Favorite::getFavoriteId, favorite.getFavoriteId())
                .set(Favorite::getName, favorite.getName())
                .set(Favorite::getDelFlag, favorite.getDelFlag());

        return favoriteMapper.update(favorite, wrapper);
    }

    @Override
    public int insertFavorite(Favorite favorite) {
        return favoriteMapper.insert(favorite);
    }
}
