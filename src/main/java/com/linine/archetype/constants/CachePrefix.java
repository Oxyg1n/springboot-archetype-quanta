package com.linine.archetype.constants;

/**
 * 缓存key前缀
 *
 * @author Leslie Leung
 * @since 2021/11/10
 */
public class CachePrefix {
    private CachePrefix() {
        throw new IllegalStateException();
    }

    public static final String TOKEN_PREFIX = "user:%s"; // token
    public static final String VERIFICATION_REGISTER_PREFIX = "register_code:%s"; // 注册+邮箱
    public static final String VERIFICATION_RESET_PREFIX = "reset_code:%s"; // 重置+邮箱

}
