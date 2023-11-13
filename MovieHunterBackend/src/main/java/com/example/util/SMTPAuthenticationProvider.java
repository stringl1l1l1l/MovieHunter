package com.example.util;

import com.example.entity.LoginUserWithCode;
import com.example.entity.User;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;

import java.util.Objects;

public class SMTPAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsService userDetailsService;

    private RedisCache redisCache;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = (authentication.getPrincipal() == null) ? null
                : authentication.getName();
        LoginUserWithCode user = (LoginUserWithCode) userDetailsService.loadUserByUsername(email);
        if(user == null) {
            // todo:数据库中不存在该邮箱对应用户
        }
        else {
            // todo:查询缓存中验证码是否和credential一致
            String credentials = (String) authentication.getCredentials();
            String codeInCache = redisCache.getCacheObject("code:" + user.getUserId());
            if(credentials == null) {
                // todo:未输入验证码
            }
            else if (StringUtils.isEmpty(codeInCache)) {
                // todo:redis中验证码为空或失效
            }
            else if(!credentials.equals(codeInCache)){
                // todo:验证码不匹配
            }
        }
        return new SMTPAuthenticationToken(user, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
       return (SMTPAuthenticationToken.class.isAssignableFrom(authentication));
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public void setRedisCache(RedisCache redisCache) {
        this.redisCache = redisCache;
    }
}
