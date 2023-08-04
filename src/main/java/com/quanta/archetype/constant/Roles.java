package com.quanta.archetype.constant;

/**
 * 用户角色
 *
 * @author Leslie Leung
 * @since 2021/9/25
 */
public class Roles {
    private Roles() {
        throw new IllegalStateException();
    }

    public static final int ROLE_SUPER_ADMIN = 1;
    public static final int ROLE_SECOND_ADMIN = 2;
    public static final int ROLE_THIRD_ADMIN = 3;
    public static final int ROLE_USER = 4;
}
