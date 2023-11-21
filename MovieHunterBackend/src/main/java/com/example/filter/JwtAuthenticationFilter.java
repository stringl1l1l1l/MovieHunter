package com.example.filter;


import com.example.entity.LoginUser;
import com.example.util.JwtUtil;
import com.example.util.RedisCache;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * 全局token拦截器
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Resource
    private RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取token
        String token = request.getHeader("token");

        //若token不存在
        if (!StringUtils.hasText(token)) {
            //放行
            filterChain.doFilter(request, response);
            return;
        }
        //token存在，解析token
        String userId = "";
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userId = claims.getSubject();
        } catch (Exception e) {
            logger.error("异常信息：", e);
        }
        //从redis中获取对象
        String key = "login:" + userId;
        LoginUser loginUser = redisCache.getCacheObject(key);
        if (Objects.isNull(loginUser)) {
            throw new RuntimeException("用户未登录");
        }
        //将用户信息存入SecurityContext，并设置为已授权，第三个参数不修改默认调用抽象的getAuthorities()方法
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //放行
        filterChain.doFilter(request, response);
    }
}
