package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.entity.Favorite;
import com.example.entity.FavoriteMovie;
import com.example.entity.Movie;
import com.example.entity.ResponseResult;
import com.example.mapper.FavoriteMapper;
import com.example.mapper.FavoriteMovieMapper;
import com.example.mapper.MovieMapper;
import com.example.service.FavoriteService;
import com.example.util.JwtUtil;
import io.jsonwebtoken.Claims;
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
    public List<Favorite> findFavoritesByCurUser(String token) throws Exception {
        Claims claims = JwtUtil.parseJWT(token);
        String userId = claims.getSubject();

        return favoriteMapper.selectList(
                new LambdaQueryWrapper<Favorite>()
                        .eq(Favorite::getUserId, userId)
        );
    }

    @Override
    public int updateFavouriteById(Favorite favorite, String token) throws Exception {
        Claims claims = JwtUtil.parseJWT(token);
        String userId = claims.getSubject();
        favorite.setUserId(userId);

        //若当前用户已有相同名字的收藏夹
        List<Favorite> favoritesByUserId = findAllFavoritesByUserId(userId);

        int flag = 0;
        for (Favorite elem : favoritesByUserId) {
            if (elem.getName().equals(favorite.getName())) {
                flag = 1;
                break;
            }
        }

        if (flag == 1) {
            throw new Exception("已存在相同名称的收藏夹，请重新命名");
        }
        return favoriteMapper.updateById(favorite);
    }

    @Override
    public int setFavouriteById(Favorite favorite, String token) throws Exception {
        Claims claims = JwtUtil.parseJWT(token);
        String userId = claims.getSubject();
        favorite.setUserId(userId);

        //若当前用户已有相同名字的收藏夹
        List<Favorite> favoritesByUserId = findAllFavoritesByUserId(userId);

        int flag = 0;
        for (Favorite elem : favoritesByUserId) {
            if (elem.getName().equals(favorite.getName())) {
                flag = 1;
                break;
            }
        }

        if (flag == 1) {
            throw new Exception("已存在相同名称的收藏夹，请重新命名");
        }

        LambdaUpdateWrapper<Favorite> wrapper = new LambdaUpdateWrapper<>();

        wrapper.eq(Favorite::getFavoriteId, favorite.getFavoriteId())
                .set(Favorite::getName, favorite.getName())
                .set(Favorite::getUserId, favorite.getUserId());

        return favoriteMapper.update(favorite, wrapper);
    }

    @Override
    public int insertFavorite(Favorite favorite, String token) throws Exception {
        Claims claims = JwtUtil.parseJWT(token);
        String userId = claims.getSubject();
        favorite.setUserId(userId);

        //若当前用户已有相同名字的收藏夹
        List<Favorite> favoritesByUserId = findAllFavoritesByUserId(userId);

        int flag = 0;
        for (Favorite elem : favoritesByUserId) {
            if (elem.getName().equals(favorite.getName())) {
                flag = 1;
                break;
            }
        }

        if (flag == 1) {
            throw new Exception("已存在相同名称的收藏夹，请重新命名");
        }
        return favoriteMapper.insert(favorite);
    }

    @Override
    public int insertMovieToFavorite(FavoriteMovie favoriteMovie) throws Exception {
        FavoriteMovie res = favoriteMovieMapper.selectOne(new LambdaQueryWrapper<FavoriteMovie>()
                .eq(FavoriteMovie::getFavoriteId, favoriteMovie.getFavoriteId())
                .eq(FavoriteMovie::getMovieId, favoriteMovie.getMovieId()));
        if (res == null)
            return favoriteMovieMapper.insert(favoriteMovie);
        else
            throw new Exception("该电影已收藏至本收藏夹");
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
        for (FavoriteMovie elem : favoriteMovieList) {
            movieIds.add(elem.getMovieId());
        }

        if (movieIds.size() == 0)
            return new ArrayList<>();
        else
            return movieMapper.selectList(new LambdaQueryWrapper<Movie>()
                    .in(Movie::getMovieId, movieIds)
            );
    }

    public int deleteFavoriteById(Long favoriteId) {
        return favoriteMapper.deleteById(favoriteId);
    }

    @Override
    public int moveMovieToFavorite(FavoriteMovie favoriteMovie, Long newFavorite, String token) throws Exception {
        Claims claims = JwtUtil.parseJWT(token);
        String userId = claims.getSubject();

        if (favoriteMapper.selectOne(new LambdaQueryWrapper<Favorite>()
                .eq(Favorite::getFavoriteId, newFavorite)
                .eq(Favorite::getUserId, userId)) == null)
            throw new Exception("当前用户未拥有该收藏夹");

        if (favoriteMovieMapper.selectOne(new LambdaQueryWrapper<FavoriteMovie>()
                .eq(FavoriteMovie::getFavoriteId, newFavorite)
                .eq(FavoriteMovie::getMovieId, favoriteMovie.getMovieId())) != null)
            throw new Exception("该电影已收藏至本收藏夹");

        return favoriteMovieMapper.update(favoriteMovie,
                new LambdaUpdateWrapper<FavoriteMovie>()
                        .eq(FavoriteMovie::getFavoriteId, favoriteMovie.getFavoriteId())
                        .eq(FavoriteMovie::getMovieId, favoriteMovie.getMovieId())
                        .set(FavoriteMovie::getFavoriteId, newFavorite)
        );
    }
}
