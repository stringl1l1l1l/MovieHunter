package com.example.service;

import com.example.entity.Role;
import com.example.entity.UserRole;

import java.util.List;

public interface RoleService {

    List<Role> showAllRoles();

    List<Role> findAllRoles();

    List<Role> findAllRolesOfOneUser(String userId);

    int insertUserRole(UserRole userRole);

    int deleteUserRole(UserRole userRole);

    int updateRoleById(Role role);

    int deleteRoleById(Integer roleId);

    int insertRole(Role role);

    int restoreRoleById(Integer roleId);
}
