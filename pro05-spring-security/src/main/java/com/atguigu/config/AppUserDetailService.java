package com.atguigu.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class AppUserDetailService implements UserDetailsService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public UserDetails loadUserByUsername(/*表单提交的用户名*/String username) throws UsernameNotFoundException {
        String sql = "SELECT id, loginacct, userpswd, username, email, createtime FROM t_admin WHERE loginacct = ?";
        Map<String, Object> resultMap = jdbcTemplate.queryForMap(sql, username);
        String loginacct = resultMap.get("loginacct").toString();
        String userpswd = resultMap.get("userpswd").toString();
        List<GrantedAuthority> list = AuthorityUtils.createAuthorityList("ROLE_学徒", "内门弟子");
        /*List<GrantedAuthority> list = new ArrayList<>();
        list.add(new SimpleGrantedAuthority("ROLE_学徒"));
        list.add(new SimpleGrantedAuthority("内门弟子"));*/
        User user = new User(loginacct, userpswd, list);

        return user;
    }
}
