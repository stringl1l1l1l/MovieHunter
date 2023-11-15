package com.example.service;

import com.example.entity.Favorite;
import com.example.entity.FavoriteMovie;
import com.example.entity.Movie;

import java.util.List;

public interface FavoriteService {
    List<Favorite> findAllFavoritesByUserId(String userId);

    int updateFavouriteById(Favorite favorite);

    int setFavouriteById(Favorite favorite);

    int insertFavorite(Favorite favorite);

    int insertMovieToFavorite(FavoriteMovie favoriteMovie);

    int deleteMovieInFavorite(FavoriteMovie favoriteMovie);

    List<Movie> findAllMoviesInFavorite(Long favoriteId);
}
