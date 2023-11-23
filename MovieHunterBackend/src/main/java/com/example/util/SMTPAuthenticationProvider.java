package com.example.util;

import com.example.entity.LoginUser;
import com.example.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Component
public class SMTPAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private RedisCache redisCache;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = (authentication.getPrincipal() == null) ? null
                : authentication.getName();
        LoginUser loginUser = (LoginUser) userDetailsService.loadUserByUsername(email);
        if(loginUser == null) {
            // todo:数据库中不存在该邮箱对应用户
            throw new BadCredentialsException("数据库中不存在该邮箱对应用户");
        }
        else {
            // todo:查询缓存中验证码是否和credential一致
            String credentials = (String) authentication.getCredentials();
            String codeInCache = redisCache.getCacheObject("code:" + loginUser.getUser().getEmail());
            if(credentials == null) {
                // todo:未输入验证码
                throw new BadCredentialsException("未输入验证码");
            }
            else if (codeInCache == null || codeInCache.isEmpty()) {
                // todo:redis中验证码为空或失效
                throw new BadCredentialsException("不存在有效的验证码，请发送验证码");
            }
            else if(!credentials.equals(codeInCache)){
                // todo:验证码不匹配
                throw new BadCredentialsException("验证码不匹配");
            }
        }

        return new SMTPAuthenticationToken(loginUser, loginUser.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
       return (SMTPAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
