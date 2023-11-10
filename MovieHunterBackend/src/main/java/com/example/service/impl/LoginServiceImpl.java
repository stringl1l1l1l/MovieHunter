package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.entity.*;
import com.example.mapper.RoleMapper;
import com.example.mapper.UserInfoMapper;
import com.example.mapper.UserMapper;
import com.example.mapper.UserRoleMapper;
import com.example.service.LoginService;
import com.example.util.JwtUtil;
import com.example.util.RedisCache;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@SuppressWarnings("rawtypes")
@Transactional
@Service("loginService")
public class LoginServiceImpl implements LoginService {
    @Resource
    private AuthenticationManager authenticationManagerBean;

    @Resource
    private RedisCache redisCache;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private UserMapper userMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Override
    public ResponseResult login(User user) {
        //数据库中查询用户是否存在
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        Authentication authentication = authenticationManagerBean.authenticate(authenticationToken);
        if (Objects.isNull(authentication)) {
            throw new RuntimeException("登录失败");
        }

        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        //查询当前用户是否已经登录
//        Object cacheObject = redisCache.getCacheObject("login:" + loginUser.getUser().getUserId());
//        if(!Objects.isNull(cacheObject)){
//            return new ResponseResult(400,"您已在登录状态，无需重复登录");
//        }
        //认证通过，使用userid生成一个jwt jwt存入ResponseResult返回
        String userid = loginUser.getUser().getUserId();
        String jwt = JwtUtil.createJWT(userid);
        Map<String, String> map = new HashMap<>();
        map.put("token", jwt);
        //把完整的用户信息存入redis  userid作为key
        redisCache.setCacheObject("login:" + userid, loginUser);

        return new ResponseResult<Map>(200, "登录成功", map);
    }

    @Override
    public ResponseResult register(User user) {
        User u = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, user.getUsername()));
        if (Objects.isNull(u)) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userMapper.insert(user);

            String userId = user.getUserId();
            String jwt = JwtUtil.createJWT(userId);
            Map<String, String> map = new HashMap<>();
            map.put("token", jwt);
            //授予该用户权限
            List<String> list = roleMapper.selectPermByRoleName("用户");
            redisCache.setCacheObject("login:" + userId, new LoginUser(user, list));
            //在user_role表中添加用户与角色的关联
            userRoleMapper.insert(new UserRole(user.getUserId(),
                    roleMapper.selectOne(
                                    new LambdaQueryWrapper<Role>()
                                            .eq(Role::getRoleName, "用户"))
                            .getRoleId())
            );
            // 在user_info表中添加用户个人信息
            userInfoMapper.insert(new UserInfo(null, user.getUserId(), null, "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif", user.getUsername()));
            return new ResponseResult<Map>(200, "注册成功", map);
        }
        return new ResponseResult<>(500, "用户名已存在,请登录");
    }

    @Override
    public ResponseResult logout() {
        //从SecurityContext中获取token
        UsernamePasswordAuthenticationToken authenticationToken
                = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authenticationToken.getPrincipal();
        String userId = loginUser.getUser().getUserId();
        //删除redis中的值
        redisCache.deleteObject("login:" + userId);
        return new ResponseResult<>(200, "注销成功");
    }
}
