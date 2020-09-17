package com.atguigu.config;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

@Component
public class AppPasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence rawPassword) {
        return privateEncoded(rawPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        // 1.对明文密码进行加密
        String formPassword = privateEncoded(rawPassword);

        // 2.声明数据库密码
        String databasePassword = encodedPassword;

        // 3.比较
        return Objects.equals(formPassword, databasePassword);
    }

    private String privateEncoded(CharSequence rawPassword) {
        try {
            // 1.创建MessageDigest对象
            String algorithm = "MD5";
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 2.获取rawPassword的字节数组
            byte[] input = ((String) rawPassword).getBytes();

            // 3.加密
            byte[] output = md.digest(input);

            // 4.转换为16进制数对应的字符
            String encoded = new BigInteger(1, output).toString(16).toUpperCase();

            return encoded;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
