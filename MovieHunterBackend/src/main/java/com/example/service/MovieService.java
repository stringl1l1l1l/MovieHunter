package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.entity.Movie;

import java.util.List;

public interface MovieService {
//    List<Movie> showAllMovies();

    List<Movie> findAllMovies();

    Movie findMovieById(Long id);

    List<Movie> findMoviesByGenresMask(int mask);

    IPage<Movie> findMoviesByPages(int pageNum, int pageSize);

    List<Movie> findMoviesByRegionsMask(int mask);
}
