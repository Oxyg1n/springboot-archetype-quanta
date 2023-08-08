package com.quanta.archetype.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密工具类
 *
 * @author gdufs
 * @since 2021/11/28
 */
@Slf4j
@Component
public class EncryptUtils {

    // 私有化构造函数，防止new对象
    private EncryptUtils() {
    }

    /**
     * MD5加密基础方法
     *
     * @param string 需要加密的字符串
     * @return 加密后的字符串
     */
    public static String md5(String string) {
        try {
            MessageDigest md = MessageDigest.getInstance("md5");
            byte[] bytes = md.digest(string.getBytes());
            return DigestUtils.md5DigestAsHex(bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * MD5加密基础方法
     *
     * @param string 需要加密的字符串
     * @param salt   加密盐值
     * @return 加密后的字符串
     */
    public static String md5(String string, String salt) {
        try {
            String sb = string + salt;
            MessageDigest md = MessageDigest.getInstance("md5");
            byte[] bytes = md.digest(sb.getBytes());
            return DigestUtils.md5DigestAsHex(bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 生成随机的盐值
     */
    public String getSalt() {
        String str = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            int temp = (int) (Math.random() * (str.length()));
            sb.append(str.charAt(temp));
        }
        return sb.toString();
    }

    /**
     * 手机号脱敏
     * 例如 138****4894
     *
     * @param mobile 完整手机号
     * @return 脱敏后的手机号
     */
    public static String mobileEncrypt(String mobile) {
        if (!StringUtils.hasText(mobile) || (mobile.length() != 11)) {
            return mobile;
        }
        return mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

}
