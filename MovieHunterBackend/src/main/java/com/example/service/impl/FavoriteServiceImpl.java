package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.entity.Favorite;
import com.example.entity.FavoriteMovie;
import com.example.entity.Movie;
import com.example.mapper.FavoriteMapper;
import com.example.mapper.FavoriteMovieMapper;
import com.example.mapper.MovieMapper;
import com.example.service.FavoriteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@Transactional
@Service("favoriteService")
public class FavoriteServiceImpl implements FavoriteService {

    @Resource
    private FavoriteMapper favoriteMapper;

    @Resource
    private FavoriteMovieMapper favoriteMovieMapper;

    @Resource
    private MovieMapper movieMapper;

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

    @Override
    public int insertMovieToFavorite(FavoriteMovie favoriteMovie) {
        return favoriteMovieMapper.insert(favoriteMovie);
    }

    @Override
    public int deleteMovieInFavorite(FavoriteMovie favoriteMovie) {
        return favoriteMovieMapper.delete(
                new LambdaQueryWrapper<FavoriteMovie>()
                        .eq(FavoriteMovie::getFavoriteId, favoriteMovie.getFavoriteId())
                        .eq(FavoriteMovie::getMovieId, favoriteMovie.getMovieId())
        );
    }

    @Override
    public List<Movie> findAllMoviesInFavorite(Long favoriteId) {
        List<FavoriteMovie> favoriteMovieList = favoriteMovieMapper.selectList(
                new LambdaQueryWrapper<FavoriteMovie>()
                        .eq(FavoriteMovie::getFavoriteId, favoriteId)
        );

        List<Long> movieIds = new ArrayList<>();
        for (FavoriteMovie elem : favoriteMovieList ) {
            movieIds.add(elem.getMovieId());
        }

        return movieMapper.selectList(new LambdaQueryWrapper<Movie>()
                .in(Movie::getMovieId, movieIds)
        );
    }
}
