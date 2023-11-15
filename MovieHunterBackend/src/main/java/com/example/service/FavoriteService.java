package com.example.service;

import com.example.entity.Favorite;

import java.util.List;

public interface FavoriteService {
    List<Favorite> findAllFavoritesByUserId(String userId);

    int updateFavouriteById(Favorite favorite);

    int setFavouriteById(Favorite favorite);

    int insertFavorite(Favorite favorite);
}
