package com.example.controller;


import com.example.entity.*;
import com.example.service.MenuService;
import com.example.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@SuppressWarnings("rawtypes")
@Api
@Validated
@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Resource
    private MenuService menuService;

    @Resource
    private RoleService roleService;

    @PreAuthorize(value = "hasAuthority('sys:manager')")
    @GetMapping("/showAllRoles")
    public ResponseResult showAllRoles() {
        return new ResponseResult<>(200, "操作成功", roleService.showAllRoles());
    }

    @PreAuthorize(value = "hasAuthority('sys:manager')")
    @GetMapping("/findAllRoles")
    public ResponseResult findAllRoles() {
        return new ResponseResult<>(200, "操作成功", roleService.findAllRoles());
    }

    @PreAuthorize(value = "hasAuthority('sys:manager')")
    @GetMapping("/showAllRolePermission")
    public ResponseResult showAllRolePermission() {
        return new ResponseResult<>(200, "操作成功", menuService.showAllRolePermission());
    }

    @PreAuthorize(value = "hasAuthority('sys:manager')")
    @GetMapping("/showPermissionMenu")
    public ResponseResult showPermissionMenu() {
        return new ResponseResult<>(200, "操作成功", menuService.showPermissionMenu());
    }

    @PreAuthorize(value = "hasAuthority('sys:manager')")
    @GetMapping("/findPermissionMenu")
    public ResponseResult findPermissionMenu() {
        return new ResponseResult<>(200, "操作成功", menuService.findPermissionMenu());
    }

    @PreAuthorize(value = "hasAuthority('sys:manager')")
    @GetMapping("/findAllRolesOfOneUser/{id}")
    public ResponseResult findAllRolesOfOneUser(@PathVariable String id) {
        return new ResponseResult<>(200, "操作成功", roleService.findAllRolesOfOneUser(id));
    }

    @PreAuthorize(value = "hasAuthority('sys:manager')")
    @PostMapping("/insertUserRole")
    public ResponseResult insertUserRole(@RequestBody List<UserRole> list) {
        int res = 0;
        for (UserRole userRole : list) {
            res += roleService.insertUserRole(userRole);
        }
        Map<String, Integer> map = new HashMap<>();
        map.put("影响行数", res);
        return new ResponseResult<>(200, "操作成功", map);
    }

    @PreAuthorize(value = "hasAuthority('sys:manager')")
    @PostMapping("/deleteUserRole")
    public ResponseResult deleteUserRole(@RequestBody List<UserRole> list) {
        int res = 0;
        for (UserRole userRole : list) {
            res += roleService.deleteUserRole(userRole);
        }
        Map<String, Integer> map = new HashMap<>();
        map.put("影响行数", res);
        return new ResponseResult<>(200, "操作成功", map);
    }

    @ApiOperation("")
    @PreAuthorize(value = "hasAuthority('sys:manager')")
    @DeleteMapping("/deleteRoleById/{id}")
    public ResponseResult deleteRoleById(@PathVariable String id) {
        int res = roleService.deleteRoleById(Integer.parseInt(id));
        Map<String, Integer> map = new HashMap<>();
        map.put("影响行数", res);
        return new ResponseResult<>(200, "操作成功", map);
    }

    @ApiOperation("")
    @PreAuthorize(value = "hasAuthority('sys:manager')")
    @PutMapping("/updateRoleById")
    public ResponseResult updateRoleById(@RequestBody Role role) {
        int res = roleService.updateRoleById(role);
        Map<String, Integer> map = new HashMap<>();
        map.put("影响行数", res);
        return new ResponseResult<>(200, "操作成功", map);
    }

    @ApiOperation("")
    @PreAuthorize(value = "hasAuthority('sys:manager')")
    @PostMapping("/insertRole")
    public ResponseResult insertRole(@RequestBody Role role) {
        int res = roleService.insertRole(role);
        Map<String, Integer> map = new HashMap<>();
        map.put("影响行数", res);
        return new ResponseResult<>(200, "操作成功", map);
    }

    @ApiOperation("")
    @PreAuthorize(value = "hasAuthority('sys:manager')")
    @GetMapping("/restoreRoleById/{id}")
    public ResponseResult restoreRoleById(@PathVariable String id) {
        int res = roleService.restoreRoleById(Integer.parseInt(id));
        Map<String, Integer> map = new HashMap<>();
        map.put("影响行数", res);
        return new ResponseResult<>(200, "操作成功", map);
    }

    @ApiOperation("")
    @PreAuthorize(value = "hasAuthority('sys:manager')")
    @GetMapping("/findAllPermissionOfOneRole/{id}")
    public ResponseResult findAllPermissionOfOneRole(@PathVariable String id) {
        return new ResponseResult<>(200, "操作成功", menuService.findAllPermissionOfOneRole(Integer.parseInt(id)));
    }

    @PreAuthorize(value = "hasAuthority('sys:manager')")
    @PostMapping("/insertRoleMenu")
    public ResponseResult insertRoleMenu(@RequestBody List<RoleMenu> list) {
        int res = 0;
        for (RoleMenu roleMenu : list) {
            res += menuService.insertRoleMenu(roleMenu);
        }
        Map<String, Integer> map = new HashMap<>();
        map.put("影响行数", res);
        return new ResponseResult<>(200, "操作成功", map);
    }

    @PreAuthorize(value = "hasAuthority('sys:manager')")
    @PostMapping("/deleteRoleMenu")
    public ResponseResult deleteRoleMenu(@RequestBody List<RoleMenu> list) {
        int res = 0;
        for (RoleMenu roleMenu : list) {
            res += menuService.deleteRoleMenu(roleMenu);
        }
        Map<String, Integer> map = new HashMap<>();
        map.put("影响行数", res);
        return new ResponseResult<>(200, "操作成功", map);
    }

    @ApiOperation("")
    @PreAuthorize(value = "hasAuthority('sys:manager')")
    @DeleteMapping("/deleteMenuById/{id}")
    public ResponseResult deleteMenuById(@PathVariable String id) {
        int res = menuService.deleteMenuById(Integer.parseInt(id));
        Map<String, Integer> map = new HashMap<>();
        map.put("影响行数", res);
        return new ResponseResult<>(200, "操作成功", map);
    }

    @ApiOperation("")
    @PreAuthorize(value = "hasAuthority('sys:manager')")
    @PutMapping("/updateMenuById")
    public ResponseResult updateMenuById(@RequestBody Menu menu) {
        int res = menuService.updateMenuById(menu);
        Map<String, Integer> map = new HashMap<>();
        map.put("影响行数", res);
        return new ResponseResult<>(200, "操作成功", map);
    }

    @ApiOperation("")
    @PreAuthorize(value = "hasAuthority('sys:manager')")
    @PostMapping("/insertMenu")
    public ResponseResult insertMenu(@RequestBody Menu menu) {
        int res = menuService.insertMenu(menu);
        Map<String, Integer> map = new HashMap<>();
        map.put("影响行数", res);
        return new ResponseResult<>(200, "操作成功", map);
    }

    @ApiOperation("")
    @PreAuthorize(value = "hasAuthority('sys:manager')")
    @GetMapping("/restoreMenuById/{id}")
    public ResponseResult restoreMenuById(@PathVariable String id) {
        int res = menuService.restoreMenuById(Integer.parseInt(id));
        Map<String, Integer> map = new HashMap<>();
        map.put("影响行数", res);
        return new ResponseResult<>(200, "操作成功", map);
    }
}
