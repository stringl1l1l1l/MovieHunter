package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Movie;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MovieMapper extends BaseMapper<Movie> {

//    @Select("SELECT * FROM movie WHERE (genres_mask & #{mask}) > 0 AND del_flag = 0")
//    List<Movie> findMoviesByGenresMask(int mask, int pageNum, int pageSize);
//
//    @Select("SELECT * FROM movie WHERE (regions_mask & #{mask}) > 0 AND del_flag = 0")
//    List<Movie> findMoviesByRegionsMask(int mask, int pageNum, int pageSize);
}
