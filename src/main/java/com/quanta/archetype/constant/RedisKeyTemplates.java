package com.quanta.archetype.constant;

/**
 * redis缓存key格式化模板
 *
 * @author Leslie Leung
 * @author Linine
 * @version 1.1
 * @since 2021/11/10
 */
public enum RedisKeyTemplates {

    /**
     * user+userId
     */
    TOKEN("user:{0}"),
    /**
     * register_code+邮箱
     */
    VERIFICATION_REGISTER_PREFIX("register_code:{0}"),
    /**
     * reset_code+邮箱
     */
    VERIFICATION_RESET_PREFIX("reset_code:{0}");


    private final String template;

    public String getTemplate() {
        return template;
    }

    RedisKeyTemplates(String template) {
        this.template = template;
    }
}
