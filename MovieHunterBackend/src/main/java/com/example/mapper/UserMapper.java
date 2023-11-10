package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface UserMapper extends BaseMapper<User>{
    @Select("select * from user")
    List<User> showAllUsers();

    @Select("select * from user where user_id= #{userId}")
    User showUserById(@Param("userId") Long userId);

    @Update("UPDATE user SET del_flag = 0 WHERE user_id = #{userId}")
    int restoreUserById(@Param("userId") Long userId);
}
