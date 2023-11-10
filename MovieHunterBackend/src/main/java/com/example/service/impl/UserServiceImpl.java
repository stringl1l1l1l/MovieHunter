package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.mapper.UserInfoMapper;
import com.example.mapper.UserMapper;
import com.example.entity.User;
import com.example.service.UserService;
import com.example.util.JwtUtil;


import io.jsonwebtoken.Claims;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Transactional
@Service("userService")
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private UserInfoMapper userInfoMapper;


    public IPage<User> showUsersByPages(int pageNum, int pageSize) {
        return userMapper.selectPage(new Page<>(pageNum, pageSize), null);
    }

    @Override
    public List<User> showAllUsers() {
        return userMapper.showAllUsers();
    }

    @Override
    public List<User> findAllUsers() {
        return userMapper.selectList(new LambdaQueryWrapper<>());
    }

    @Override
    public User findUserById(Long id) {
        return userMapper.selectById(id);
    }

    @Override
    public User findUserByPhoneNum(String phone) {
        return userMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getPhone, phone)
        );
    }

    @Override
    public User findUserByUsername(String username) {
        return userMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getUsername, username)
        );
    }

    @Override
    public List<User> findUsersByMap(Map<String, Object> map) {
        return userMapper.selectByMap(map);
    }

    @Override
    public int deleteUserById(Long id) {
        return userMapper.deleteById(id);
    }

    @Override
    public int deleteUserByMap(Map<String, Object> map) {
        return userMapper.deleteByMap(map);
    }

    @Override
    public int updateUserById(User user) {
        if (StringUtils.hasText(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(User::getUserId, user.getUserId());
        return userMapper.update(user, wrapper);
    }

    @Override
    public int setUserById(User user) {
        if (StringUtils.hasText(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(User::getUserId, user.getUserId())
                .set(User::getUsername, user.getUsername())
                .set(User::getPhone, user.getPhone())
                .set(User::getPassword, user.getPassword())
                .set(User::getDelFlag, user.getDelFlag());

        return userMapper.update(user, wrapper);
    }

    @Override
    public int insertUser(User user) {
        return userMapper.insert(user);
    }

    @Override
    public List<String> getUserRoles(String token) {
        try {
            Claims claims = JwtUtil.parseJWT(token);
            long id = Long.parseLong(claims.getSubject());
            return userInfoMapper.getUserRoles(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int restoreUserById(Long userId) {
        return userMapper.restoreUserById(userId);
    }

    @Override
    public User showUserById(Long userId) {
        return userMapper.showUserById(userId);
    }

    @Override
    public User parseToken(String token) {
        try {
            Claims claims = JwtUtil.parseJWT(token);
            long id = Long.parseLong(claims.getSubject());
            return userMapper.selectById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map<String, Object> getUserInfo(String token) {
        try {
            Claims claims = JwtUtil.parseJWT(token);
            long id = Long.parseLong(claims.getSubject());
            return userInfoMapper.getUserInfo(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
