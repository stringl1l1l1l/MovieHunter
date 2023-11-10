package com.example.service;

import com.example.entity.Menu;
import com.example.entity.RoleMenu;

import java.util.List;
import java.util.Map;

public interface MenuService {
    List<String> selectPermByUserId(String userId);

    List<Map<String,Object>> showAllRolePermission();

    List<Menu> showPermissionMenu();

    List<Menu> findPermissionMenu();

    List<Menu> findAllPermissionOfOneRole(Integer roleId);

    int insertRoleMenu(RoleMenu roleMenu);

    int deleteRoleMenu(RoleMenu roleMenu);

    int updateMenuById(Menu menu);

    int deleteMenuById(Integer menuId);

    int insertMenu(Menu menu);

    int restoreMenuById(Integer menuId);
}
