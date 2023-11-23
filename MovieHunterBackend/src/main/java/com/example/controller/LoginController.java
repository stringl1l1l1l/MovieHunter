package com.example.controller;

import com.example.entity.LoginUserWithCode;
import com.example.entity.LoginUserWithCodePwd;
import com.example.entity.LoginUserWithPwd;
import com.example.entity.ResponseResult;
import com.example.jsr303.LoginOperation;
import com.example.service.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@SuppressWarnings("rawtypes")
@Api
@Validated
@RestController
public class LoginController {
    @Resource
    private LoginService loginService;

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @ApiOperation("登录，并返回token")
    @PostMapping("/loginWithPwd")
    public ResponseResult loginWithPwd(@RequestBody @Valid LoginUserWithPwd user) {
        ResponseResult result = loginService.loginWithPwd(user);
        if (result.getCode() == 200)
            logger.info("用户 { " + user.getEmail() + "} 登录成功");
        return result;
    }

    @ApiOperation("使用邮箱密码注册，通过验证码验证邮箱后返回token")
    @PostMapping("/register")
    public ResponseResult register(@RequestBody @Valid LoginUserWithCodePwd user) {
        ResponseResult result = loginService.register(user);
        if (result.getCode() == 200)
            logger.info("用户 { " + user.getEmail() + "} 注册成功");
        return result;
    }

    @ApiOperation("注销")
    @GetMapping("/logOut")
    public ResponseResult logOut() {
        ResponseResult result = loginService.logout();
        if (result.getCode() == 200)
            logger.info("注销成功");
        return result;
    }

    @ApiOperation("发送验证码")
    @GetMapping("/sendMsg/{email}")
    public ResponseResult sendMsg(@PathVariable String email) {
        ResponseResult result = loginService.sendMsg(email);
        if (result.getCode() == 200)
            logger.info("邮箱 { " + email + "} 验证码发送成功");
        return result;
    }

    @ApiOperation("验证码登录")
    @PostMapping("/loginWithCode")
    public ResponseResult loginWithCode(@RequestBody @Valid LoginUserWithCode user) {
        return loginService.loginWithCode(user);
    }

    @ApiOperation("重设密码")
    @PostMapping("/resetPwdWithCode")
    public ResponseResult resetPwdWithCode(@RequestBody @Valid LoginUserWithCodePwd user) {
        return loginService.resetPwdWithCode(user);
    }
}