package com.quanta.archetype.util;

import com.quanta.archetype.constant.RedisKeyTemplates;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * token工具类
 *
 * @author Leslie Leung
 * @author Linine
 * @since 2021/9/26
 */
@Component
public class TokenUtils {

    @Value("${user.tokenLifeDays:7}")
    public int tokenLifeDays;  // token有效期默认为七天

    private final RedisUtils redisUtils;

    public TokenUtils(RedisUtils redisUtils) {
        this.redisUtils = redisUtils;
    }

    /**
     * 发放token
     *
     * @param uid  用户id
     * @param role 权限
     * @return 获取到的token
     */
    public String grantToken(int uid, int role) {
        HashMap<String, Integer> identity = new HashMap<>();
        identity.put("uid", uid);
        identity.put("role", role);
        String key = UUID.randomUUID().toString().replace("-", "");
        redisUtils.set(key, identity, tokenLifeDays, TimeUnit.DAYS);
        // 删掉之前如果有效的token
        String formerToken = (String) redisUtils.get(redisUtils.generateKey(RedisKeyTemplates.TOKEN, String.valueOf(uid)));
        if (formerToken != null) {
            destroyToken(formerToken);
        }
        // 单点登录验证
        redisUtils.set(redisUtils.generateKey(RedisKeyTemplates.TOKEN, String.valueOf(uid)), key);
        return key;

    }

    /**
     * 获取token信息
     *
     * @param key token
     * @return uid和role的map
     */
    @SuppressWarnings("unchecked")
    private HashMap<String, Integer> retrieveToken(String key) {
        Object o = redisUtils.get(key);
        return o == null ? null : (HashMap<String, Integer>) o;
    }

    /**
     * 获取token对应的权限
     *
     * @param key token
     * @return 权限
     */
    public Integer getTokenRole(String key) {
        HashMap<String, Integer> map = retrieveToken(key);
        return map == null ? null : map.get("role");
    }

    /**
     * 获取token对应的id
     *
     * @param key token
     * @return id
     */
    public Integer getTokenUid(String key) {
        HashMap<String, Integer> map = retrieveToken(key);
        return map == null ? null : map.get("uid");
    }

    /**
     * 删除token
     *
     * @param key 缓存key
     */
    private void destroyToken(String key) {
        redisUtils.del(key);
    }

    /**
     * 刷新token时间
     */
    public void refreshToken(String token, int uid) {
        Long last = redisUtils.getExpire(token, TimeUnit.DAYS); // 获取token剩余时间
        if (last < 1) { // 有效期小于一天
            // 更新有效期
            redisUtils.expire(token, tokenLifeDays, TimeUnit.DAYS);
            redisUtils.expire(redisUtils.generateKey(RedisKeyTemplates.TOKEN, String.valueOf(uid)), tokenLifeDays, TimeUnit.DAYS);
        }
    }

    /**
     * 安全退出
     *
     * @param uid uid
     */
    public void safeExit(int uid) {
        String formerToken = (String) redisUtils.get(redisUtils.generateKey(RedisKeyTemplates.TOKEN, String.valueOf(uid)));
        redisUtils.del(redisUtils.generateKey(RedisKeyTemplates.TOKEN, String.valueOf(uid)));
        destroyToken(formerToken);
    }
}
