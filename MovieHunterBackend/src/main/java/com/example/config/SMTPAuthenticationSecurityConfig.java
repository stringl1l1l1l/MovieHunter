//package com.example.config;
//
//import com.example.filter.SMTPAuthenticationFilter;
//import com.example.util.SMTPAuthenticationProvider;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.password.NoOpPasswordEncoder;
//import org.springframework.security.web.DefaultSecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//
//@Component
//@RequiredArgsConstructor
//public class SMTPAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
//
//    @Autowired
//    private UserDetailsService userDetailsService;
//
//    @Autowired
//    private SMTPAuthenticationFilter smtpAuthenticationFilter;
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        //自定义provider及service，一套身份认证
//        auth.authenticationProvider(getAuthenticationProvider())
//                //使用系统自带provider，及自定义service，另一套认证
//                .userDetailsService(userDetailsService).passwordEncoder(NoOpPasswordEncoder.getInstance());
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                //关闭csrf
//                .csrf().disable()
//                //不通过Session获取SecurityContext
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .authorizeRequests()
//                // 对于登录接口 使用匿名访问
//                .antMatchers("/user/getUserInfo/**"
//                        , "/user/parseToken/**").permitAll()
//                .antMatchers("/login*",
//                        "/register*",
//                        "/sendMsg/**",
//                        "/doc.html",
//                        "/swagger-ui.html",
//                        "/webjars/**",
//                        "/swagger-resources/**",
//                        "/v2/**",
//                        "/charts"
//                ).anonymous()
//                // 除上面外的所有请求全部需要鉴权认证
//                .anyRequest().authenticated();
//
//        //添加token过滤器到security过滤器链中
//        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//
//        //配置异常处理器
//        http.exceptionHandling()
//                //配置认证失败处理器
//                .authenticationEntryPoint(authenticationEntryPoint)
//                .accessDeniedHandler(accessDeniedHandler);
//
//
//        //允许跨域
//        http.cors();
//    }
//
//    /**
//     * 自定义provider，注入自定义service
//     */
//    public SMTPAuthenticationProvider getAuthenticationProvider() {
//        return new SMTPAuthenticationProvider();
//    }
//}