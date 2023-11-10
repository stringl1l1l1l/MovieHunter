package com.example.service;

import com.example.entity.ResponseResult;
import com.example.entity.User;

public interface LoginService {
    ResponseResult login(User user);

    ResponseResult register(User user);

    ResponseResult logout();
}
