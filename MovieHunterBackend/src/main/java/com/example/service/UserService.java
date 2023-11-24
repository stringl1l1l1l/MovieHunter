package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.entity.User;

import java.util.List;
import java.util.Map;


public interface UserService {
    List<User> findAllUsers();

    User findUserById(String id);

    User findUserByUsername(String username);

    User findUserByEmail(String email);

//    User findUserByPhoneNum(String phone);

    List<User> findUsersByMap(Map<String, Object> map);

    int deleteUserById(String id);

    int deleteUserByMap(Map<String, Object> map);

    int updateUserById(User user) throws Exception;

    int setUserById(User user) throws Exception;

    int insertUser(User user);

    Map<String, Object> getUserInfo(String token);

    User parseToken(String token);

    List<String> getUserRoles(String token);

    List<User> showAllUsers();

    User showUserById(Long userId);

    int restoreUserById(Long userId);

    IPage<User> showUsersByPages(int pageNum, int pageSize);
}
