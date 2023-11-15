package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.Movie;
import com.example.entity.User;
import com.example.mapper.MovieMapper;
import com.example.service.MovieService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Transactional
@Service("movieService")
public class MovieServiceImpl implements MovieService {

    @Resource
    private MovieMapper movieMapper;

    @Override
    public IPage<Movie> findAllMovies(Integer pageNum, Integer pageSize) {
        return movieMapper.selectPage(new Page<>(pageNum, pageSize), null);
    }

    @Override
    public Movie findMovieById(Long id) {
        return movieMapper.selectOne(
                new LambdaQueryWrapper<Movie>()
                        .eq(Movie::getMovieId, id)
        );
    }

    @Override
    public IPage<Movie> findMoviesByGenresMask(int mask, int pageNum, int pageSize) {
       return movieMapper.selectPage(new Page<>(pageNum, pageSize),
               new LambdaQueryWrapper<Movie>().apply("(genres_mask & {0}) > 0", mask)
       );
    }

    @Override
    public IPage<Movie> findMoviesByPages(int pageNum, int pageSize) {
        return movieMapper.selectPage(new Page<>(pageNum, pageSize), null);
    }

    @Override
    public IPage<Movie> findMoviesByRegionsMask(int mask, int pageNum, int pageSize) {
        return movieMapper.selectPage(new Page<>(pageNum, pageSize),
                new LambdaQueryWrapper<Movie>().apply("(regions_mask & {0}) > 0", mask)
        );
    }
}
