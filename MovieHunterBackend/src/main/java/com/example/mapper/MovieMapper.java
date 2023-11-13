package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Movie;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MovieMapper extends BaseMapper<Movie> {

    @Select("SELECT * FROM movie WHERE del_flag = 0 AND (genres_mask & #{mask} > 0)")
    List<Movie> findMoviesByGenresMask(int mask);
}
