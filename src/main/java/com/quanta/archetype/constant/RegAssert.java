package com.quanta.archetype.constant;

import lombok.Getter;

/**
 * 正则表达式常量
 *
 * @author Linine
 * @since 2022/11/19 21:17
 */
@Getter
public enum RegAssert {

    NUMBER("[1-9][0-9]{10}", "学号格式错误"),
    EMAIL("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$", "邮箱格式错误"),
    PHONE("^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$", "手机号格式错误");


    RegAssert(String regStr, String msg) {
        this.regStr = regStr;
        this.msg = msg;
    }

    private final String regStr;

    private final String msg;
}
