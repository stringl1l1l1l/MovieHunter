package com.example.service;

import com.example.entity.LoginUserWithCode;
import com.example.entity.LoginUserWithPwd;
import com.example.entity.ResponseResult;
import com.example.entity.User;

public interface LoginService {
    ResponseResult login(LoginUserWithPwd user);

//    ResponseResult register(LoginUserWithPwd user);

    ResponseResult logout();

    ResponseResult sendMsg(String email);

    ResponseResult loginWithCode(LoginUserWithCode user);

    ResponseResult registerWithCode(LoginUserWithCode user);
}
