package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.UserInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface UserInfoMapper extends BaseMapper<UserInfo> {

    @Select("SELECT introduction, avatar, name FROM user_info WHERE user_id = #{userId}")
    Map<String, Object> getUserInfo(@Param("userId") String userId);

    @Select("SELECT role_name FROM view_user_role WHERE user_id = #{userId}")
    List<String> getUserRoles(@Param("userId") String userId);

}
