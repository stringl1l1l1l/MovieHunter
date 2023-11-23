package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Role;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

public interface RoleMapper extends BaseMapper<Role> {

    @Select("SELECT DISTINCT permission FROM view_role_perm WHERE role_name = #{roleName}")
    List<String> selectPermByRoleName(@Param("roleName") String roleName);

    @Select("SELECT permission FROM  view_role_perm WHERE role_id = #{roleId}")
    List<Map<String, Object>> selectPermByRoleId(@Param("roleId") Integer roleId);

    @Select("SELECT * FROM role")
    List<Role> showAllRoles();

    @Select("SELECT * FROM view_user_role WHERE user_id = #{userId}")
    List<Role> findAllRolesOfOneUser(@Param("userId") String userId);

    @Update("UPDATE role SET del_flag = 0 WHERE role_id = #{roleId}")
    int restoreRoleById(@Param("roleId") Integer roleId);
}
