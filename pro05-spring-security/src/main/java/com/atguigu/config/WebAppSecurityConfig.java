package com.atguigu.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class WebAppSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private AppUserDetailService userDetailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean("passwordEncoder")
    public BCryptPasswordEncoder getBCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity security) throws Exception {

        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);

        security
                .authorizeRequests()                                        // 对请求进行授权
                .antMatchers("/layui/**", "/index.jsp")        // 针对静态资源和/index.jsp进行授权
                .permitAll()                                                // 可以无条件访问
                .antMatchers("/level1/**")                     // 针对/level1/**设置访问要求
                .hasRole("学徒")                                            // 要求用户具备学徒的角色才可以访问
                .antMatchers("/level2/**")                     // 针对/level2/**设置访问要求
                .hasAnyAuthority("内门弟子")                    // 要求用户具备内门弟子的权限才可以访问
                .antMatchers("/level3/**")                     // 针对/level3/**设置访问要求
                .hasRole("宗师")                                            // 要求用户具备宗师的角色才可以访问
                .and()
                .authorizeRequests()                                        // 对请求进行授权
                .anyRequest()                                               // 任意请求
                .authenticated()                                            // 需要登录以后才可以访问
                // 登录
                .and()
                .formLogin()                                                // 使用表单形式登录
                .loginPage("/index.jsp")                                    // 指定登录页面（如果没有指定会访问SpringSecurity自带的登录页）
                .loginProcessingUrl("/do/login.html")                       // 指定提交登录表单的地址
                .permitAll()                                                // 登录地址本身也需要permitAll()放行
                .usernameParameter("loginAcct")                             // 定制登录账号请求参数名
                .passwordParameter("userPswd")                              // 定制登录密码请求参数名
                .defaultSuccessUrl("/main.html")                            // 设置登录成功后默认前往的URL地址
                // 注销
                .and()
                .logout()                                                   // 开启退出功能
                .logoutUrl("/do/logout.html")                               // 指定退出请求的url地址
                .logoutSuccessUrl("/index.jsp")                             // 退出成功后前往的地址
                // 前往自定义403界面
                .and()
                .exceptionHandling()                                        // 指定异常处理器
                //.accessDeniedPage("/to/no/auth/page.html")                  // 访问被拒绝处理时的页面
                .accessDeniedHandler(new AccessDeniedHandler() {
                    @Override
                    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
                        request.setAttribute("message", "抱歉！您无法访问这个资源！☆☆☆");
                        request.getRequestDispatcher("/WEB-INF/views/no_auth.jsp").forward(request, response);
                    }
                })
                // 开启“记住我”功能
                .and()
                .rememberMe()                                               // 开启“记住我”功能
                // 启用令牌仓库功能
                .tokenRepository(tokenRepository)
        ;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
        // 1. 内存中进行认证、加载角色权限信息
        /*builder
                .inMemoryAuthentication()       // 在内存中完成账号、密码的检查
                .withUser("tom")      // 指定账号
                .password("123456")             // 指定密码
                .roles("ADMIN", "学徒")                 // 定制当前用户的角色
                .and()
                .withUser("jerry")      // 指定账号
                .password("123456")             // 指定密码
                .authorities("UPDATE", "内门弟子")          // 指定当前用户的权限
        ;*/
        // 2. 查询数据库完成认证、加载角色权限信息 -- 默认实现
        //builder.jdbcAuthentication().usersByUsernameQuery("tom");

        // 3. 查询数据库完成认证、加载角色权限信息 -- 自定义实现
        builder
                .userDetailsService(userDetailService)
                .passwordEncoder(passwordEncoder)
        ;
    }
}
