package com.example.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.entity.ResponseResult;
import com.example.entity.User;
import com.example.jsr303.SetOperation;
import com.example.service.UserService;
import com.example.util.RedisCache;
import com.example.util.SendEmailUtils;
import com.example.util.ValidateCodeUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@SuppressWarnings("rawtypes")
@Api
@Validated
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @PreAuthorize(value = "hasAuthority('sys:get')")
    @ApiOperation("根据用户ID查询用户")
    @GetMapping("/findUserById/{id}")
    public ResponseResult findUserById(@PathVariable String id) {
        User user = userService.findUserById(id);
        if (Objects.isNull(user)) {
            return new ResponseResult(200, "用户ID不存在");
        } else
            return new ResponseResult<>(200, "操作成功", user);
    }

    @PreAuthorize(value = "hasAuthority('sys:manager')")
    @GetMapping("/showAllUsers")
    public ResponseResult showAllUsers() {
        List<User> userList = userService.showAllUsers();
        return new ResponseResult<>(200, "操作成功", userList);
    }

    @PreAuthorize(value = "hasAuthority('sys:manager')")
    @GetMapping("/showUsersByPages/{pageNum}")
    public ResponseResult showUsersByPages(@PathVariable(required = false) Integer pageNum, @RequestParam(defaultValue = "6") Integer pageSize) {
        if (pageNum == null)
            pageNum = 1;
        IPage<User> userPage = userService.showUsersByPages(pageNum, pageSize);
        return new ResponseResult<>(200, "操作成功", userPage);
    }

    @PreAuthorize(value = "hasAuthority('sys:manager')")
    @ApiOperation("根据用户名查询用户")
    @GetMapping("/findUserByUserName/{username}")
    public ResponseResult findUserByUserName(@PathVariable String username) {
        User user = userService.findUserByUsername(username);
        if (Objects.isNull(user))
            return new ResponseResult(500, "用户名不存在");
        else
            return new ResponseResult<>(200, "操作成功", user);
    }

    @PreAuthorize(value = "hasAuthority('sys:manager')")
    @ApiOperation("根据邮箱查询用户")
    @GetMapping("/findUserByEmail/{email}")
    public ResponseResult findUserByEmail(@PathVariable String email) {
        User user = userService.findUserByEmail(email);
        if (Objects.isNull(user))
            return new ResponseResult(500, "邮箱不存在");
        else
            return new ResponseResult<>(200, "操作成功", user);
    }

    @PreAuthorize(value = "hasAuthority('sys:post')")
    @ApiOperation("根据请求体中的键值对查询满足条件的所有用户, 例如：{ 'car_num'(列名) : *******(值) }")
    @PostMapping("/findUsersByMap")
    public ResponseResult findUsersByMap(@RequestBody Map<String, Object> map) {
        List<User> userList = userService.findUsersByMap(map);
        if (userList.isEmpty()) {
            return new ResponseResult(200, "未查询到结果");
        }
        return new ResponseResult<List>(200, "操作成功", userList);
    }

    @PreAuthorize(value = "hasAuthority('sys:get')")
    @ApiOperation("返回数据库中的所有用户")
    @GetMapping("/findAllUsers")
    public ResponseResult findAllUsers() {
        return new ResponseResult<List>(200, "操作成功", userService.findAllUsers());
    }


    @PreAuthorize(value = "hasAuthority('sys:put')")
    @ApiOperation("根据提供用户的ID更新对应的用户, 全量更新, null值会更新")
    @PutMapping("/updateUserById")
    public ResponseResult updateUserById(@RequestBody @Valid User user) throws Exception {
        int res = userService.updateUserById(user);
        Map<String, Integer> map = new HashMap<>();
        map.put("影响行数", res);
        return new ResponseResult<Map>(200, "操作成功", map);
    }

    @PreAuthorize(value = "hasAuthority('sys:put')")
    @ApiOperation("根据提供用户的ID覆盖对应的用户, 增量更新, null值不会更新")
    @PutMapping("/setUserById")
    public ResponseResult setUserById(@RequestBody @Valid @Validated(value = {SetOperation.class}) User user) throws Exception {
        int res = userService.setUserById(user);
        Map<String, Integer> map = new HashMap<>();
        map.put("影响行数", res);
        return new ResponseResult<Map>(200, "操作成功", map);
    }

    @PreAuthorize(value = "hasAuthority('sys:post')")
    @ApiOperation("插入一条用户信息")
    @PostMapping("/insertUser")
    public ResponseResult insertUser(@RequestBody @Valid User user) {
        //若已存在该用户名
        User userByUsername = userService.findUserByUsername(user.getUsername());
        if (!Objects.isNull(userByUsername)) {
            return new ResponseResult(200, "该用户名已存在");
        }
        int res = userService.insertUser(user);
        Map<String, Integer> map = new HashMap<>();
        map.put("影响行数", res);
        return new ResponseResult<Map>(200, "操作成功", map);
    }

    @PreAuthorize(value = "hasAuthority('sys:delete')")
    @ApiOperation("根据用户ID删除一条用户信息")
    @DeleteMapping("/deleteUserById/{id}")
    public ResponseResult deleteUserById(@PathVariable String id) {
        int res = userService.deleteUserById(id);
        Map<String, Integer> map = new HashMap<>();
        map.put("影响行数", res);
        return new ResponseResult<Map>(200, "操作成功", map);
    }

    @GetMapping("/getUserInfo/{token}")
    public ResponseResult getUserInfo(@PathVariable String token) {
        Map<String, Object> userInfo = userService.getUserInfo(token);
        if (Objects.isNull(userInfo)) {
            return new ResponseResult<>(400, "操作失败");
        } else {
            return new ResponseResult<>(200, "解析成功", userInfo);
        }
    }

    @ApiOperation("token解析")
    @GetMapping("/parseToken/{token}")
    public ResponseResult parseToken(@PathVariable String token) {
        User user = userService.parseToken(token);
        if (Objects.isNull(user)) {
            return new ResponseResult<>(400, "token解析失败");
        } else {
            return new ResponseResult<>(200, "解析成功", user);
        }
    }

    @ApiOperation("")
    @PreAuthorize(value = "hasAuthority('sys:user')")
    @GetMapping("/getUserRoles/{token}")
    public ResponseResult getUserRoles(@PathVariable String token) {
        List<String> userRoles = userService.getUserRoles(token);
        if (Objects.isNull(userRoles)) {
            return new ResponseResult<>(400, "操作失败");
        } else {
            return new ResponseResult<>(200, "解析成功", userRoles);
        }
    }

    @ApiOperation("")
    @PreAuthorize(value = "hasAuthority('sys:manager')")
    @GetMapping("/restoreUserById/{id}")
    public ResponseResult restoreUserById(@PathVariable Long id) {
        int res = userService.restoreUserById(id);
        Map<String, Integer> map = new HashMap<>();
        map.put("影响行数", res);
        return new ResponseResult<>(200, "操作成功", map);
    }

    @ApiOperation("")
    @PreAuthorize(value = "hasAuthority('sys:manager')")
    @GetMapping("/showUserById/{id}")
    public ResponseResult showUserById(@PathVariable Long id) {
        return new ResponseResult<>(200, "操作成功", userService.showUserById(id));
    }
}