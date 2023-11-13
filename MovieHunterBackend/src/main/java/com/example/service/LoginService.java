package com.example.service;

import com.example.entity.LoginUserWithCode;
import com.example.entity.LoginUserWithCodePwd;
import com.example.entity.ResponseResult;

public interface LoginService {
    ResponseResult login(LoginUserWithCodePwd user);

//    ResponseResult register(LoginUserWithPwd user);

    ResponseResult logout();

    ResponseResult sendMsg(String email);

    ResponseResult loginWithCode(LoginUserWithCode user);

    ResponseResult register(LoginUserWithCodePwd user);
}
