package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.entity.*;
import com.example.mapper.*;
import com.example.service.CommentService;
import com.example.service.LikesService;
import com.example.service.LoginService;
import com.example.util.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("rawtypes")
@Transactional
@Service("loginService")
public class LoginServiceImpl implements LoginService {
    @Resource
    private AuthenticationManager authenticationManagerBean;

    @Resource
    private RedisCache redisCache;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private UserMapper userMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Override
    public ResponseResult loginWithPwd(LoginUserWithPwd user) {
        //数据库中查询用户是否存在
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
        Authentication authentication = authenticationManagerBean.authenticate(authenticationToken);

        LoginUser loginUser = (LoginUser) authentication.getPrincipal();

        //认证通过，使用userid生成一个jwt jwt存入ResponseResult返回
        String userid = loginUser.getUser().getUserId();
        String jwt = JwtUtil.createJWT(userid);
        Map<String, String> map = new HashMap<>();
        map.put("token", jwt);
        //把完整的用户信息存入redis  userid作为key
        redisCache.setCacheObject("login:" + userid, loginUser);
        return new ResponseResult<Map>(200, "登录成功", map);
    }


    @Override
    public ResponseResult logout() {
        //从SecurityContext中获取token
        UsernamePasswordAuthenticationToken authenticationToken
                = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authenticationToken.getPrincipal();
        String userId = loginUser.getUser().getUserId();
        //删除redis中的值
        redisCache.deleteObject("login:" + userId);
        return new ResponseResult<>(200, "注销成功");
    }

    @Override
    public ResponseResult sendMsg(String email) {
        String code = null;
        try {
            // 生成随机验证码并发送到指定邮箱
            code = SendEmailUtils.sendAuthCodeEmail(email);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseResult<>(500, "验证码发送失败");
        }

        // 将验证码缓存到redis
        redisCache.setCacheObject("code:" + email, code, SendEmailUtils.LIFETIME_MIN, TimeUnit.MINUTES);
        return new ResponseResult<>(200, "验证码发送成功，请注意查收");
    }

    @Override
    public ResponseResult loginWithCode(LoginUserWithCode user) {
        SMTPAuthenticationToken authenticationToken = new SMTPAuthenticationToken(user.getEmail(), user.getCode());
        Authentication authentication = authenticationManagerBean.authenticate(authenticationToken);

        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        String userid = loginUser.getUser().getUserId();

        String jwt = JwtUtil.createJWT(userid);
        Map<String, String> map = new HashMap<>();
        map.put("token", jwt);
        //把完整的用户信息存入redis  userid作为key
        redisCache.setCacheObject("login:" + userid, loginUser);
        redisCache.deleteObject("code:" + user.getEmail());
        return new ResponseResult<Map>(200, "登录成功", map);
    }

    @Override
    public ResponseResult register(LoginUserWithCodePwd user) {
        User u = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getEmail, user.getEmail()));
        if (Objects.isNull(u)) {
            // 检查输入的验证码是否和缓存中一致，验证邮箱有效性
            String codeInCache = redisCache.getCacheObject("code:" + user.getEmail());
            if (!Objects.equals(codeInCache, user.getCode())) {
                return new ResponseResult<>(500, "验证码不匹配，请重新检查");
            }

            // 用户数据插入数据库
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User insertUser = new User(user);
            insertUser.setUsername(user.getEmail());
            userMapper.insert(insertUser);

            // 生成JWT
            String userId = insertUser.getUserId();
            String jwt = JwtUtil.createJWT(userId);
            Map<String, String> map = new HashMap<>();
            map.put("token", jwt);

            //授予该用户权限
            List<String> list = roleMapper.selectPermByRoleName("用户");
            redisCache.setCacheObject("login:" + userId, new LoginUser(new User(user), list));
            //在user_role表中添加用户与角色的关联
            userRoleMapper.insert(new UserRole(insertUser.getUserId(),
                    roleMapper.selectOne(
                                    new LambdaQueryWrapper<Role>()
                                            .eq(Role::getRoleName, "用户"))
                            .getRoleId())
            );
            redisCache.deleteObject("code:" + user.getEmail());
            userInfoMapper.insert(new UserInfo(null, userId, null,"https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif", user.getEmail()));

            return new ResponseResult<Map>(200, "注册成功", map);
        }
        return new ResponseResult<>(500, "用户已存在,请登录");
    }

    @Override
    public ResponseResult resetPwdWithCode(LoginUserWithCodePwd user) {
        // 查询用户是否存在
        User u = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getEmail, user.getEmail()));

        if (!Objects.isNull(u)) {
            // 检查输入的验证码是否和缓存中一致
            String codeInCache = redisCache.getCacheObject("code:" + user.getEmail());
            if (!Objects.equals(codeInCache, user.getCode())) {
                return new ResponseResult<>(500, "验证码不匹配或已过期，请重新检查");
            }

            User newUser = new User();
            newUser.setEmail(user.getEmail());
            newUser.setPassword(passwordEncoder.encode(user.getPassword()));

            int res = userMapper.update(newUser, new LambdaQueryWrapper<User>().eq(User::getEmail, user.getEmail()));
            Map<String, Integer> map = new HashMap<>();
            map.put("影响行数", res);

            redisCache.deleteObject("code:" + user.getEmail());
            return new ResponseResult<>(200, "密码修改成功", map);
        }
        return new ResponseResult<>(500, "该用户不存在");
    }
}
