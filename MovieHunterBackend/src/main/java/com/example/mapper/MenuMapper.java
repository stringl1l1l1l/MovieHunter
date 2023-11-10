package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Menu;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

public interface MenuMapper extends BaseMapper<Menu> {

    @Select("SELECT DISTINCT permission FROM view_user_perm WHERE user_id = #{userId}")
    List<String> selectPermByUserId(String userId);

    @Select("SELECT * FROM view_role_perm")
    List<Map<String,Object>> showAllRolePermission();

    @Select("SELECT * FROM menu")
    List<Menu> showPermissionMenu();

    @Select("SELECT permission FROM view_role_perm WHERE role_id = #{roleId}")
    List<Menu> findAllPermissionOfOneRole(@Param("roleId") Integer roleId);

    @Update("UPDATE menu SET del_flag = 0 WHERE menu_id = #{menuId}")
    int restoreMenuById(@Param("menuId") Integer menuId);
}
