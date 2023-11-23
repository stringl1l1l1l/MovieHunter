package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.entity.Movie;

import java.util.List;

public interface MovieService {
//    List<Movie> showAllMovies();

    IPage<Movie> findAllMovies(Integer pageNum, Integer pageSize);

    Movie findMovieById(Long id);

    IPage<Movie> findMoviesByGenresMask(int mask, int pageNum, int pageSize);

    IPage<Movie> findMoviesByPages(int pageNum, int pageSize);

    IPage<Movie> findMoviesByRegionsMask(int mask, int pageNum, int pageSize);

    IPage<Movie> findMoviesByName(String name, int pageNum, int pageSize);

    int deleteMovieById(Long id);

    int updateMovieById(Movie movie);

    int insertMovie(Movie movie);

    int setMovieById(Movie movie);
}
