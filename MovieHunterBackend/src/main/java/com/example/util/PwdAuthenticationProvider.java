package com.example.util;

import com.example.entity.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class PwdAuthenticationProvider implements AuthenticationProvider {
    @Resource
    private UserDetailsService userDetailsService;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = (authentication.getPrincipal() == null) ? null
                : authentication.getName();
        LoginUser loginUser = (LoginUser) userDetailsService.loadUserByUsername(email);
        if(loginUser == null) {
            // todo:数据库中不存在该邮箱对应用户
            throw new BadCredentialsException("邮箱对应用户不存在");
        }
        else {
            // todo:查询数据库密码是否和credential一致
            String credentials = (String) authentication.getCredentials();
            if(credentials == null) {
                // todo:未输入密码
                throw new BadCredentialsException("密码不能为空");
            }
            else if(!passwordEncoder.matches(loginUser.getPassword(), credentials)){
                throw new BadCredentialsException("密码错误");
            }

            return new UsernamePasswordAuthenticationToken(loginUser, loginUser.getAuthorities());
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
