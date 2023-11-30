package com.example.service;

import com.example.entity.Favorite;
import com.example.entity.FavoriteMovie;
import com.example.entity.Movie;

import java.util.List;

public interface FavoriteService {
    List<Favorite> findAllFavoritesByUserId(String userId);

    int updateFavouriteById(Favorite favorite, String token) throws Exception;

    int setFavouriteById(Favorite favorite, String token) throws Exception;

    int insertFavorite(Favorite favorite, String token) throws Exception;

    int insertMovieToFavorite(FavoriteMovie favoriteMovie) throws Exception;

    int deleteMovieInFavorite(FavoriteMovie favoriteMovie);

    List<Movie> findAllMoviesInFavorite(Long favoriteId);

    List<Favorite> findFavoritesByCurUser(String token) throws Exception;

    int deleteFavoriteById(Long favoriteId);

    int moveMovieToFavorite(FavoriteMovie favoriteMovie, Long newFavorite, String token) throws Exception;
}
