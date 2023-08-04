package com.quanta.archetype.util;

import com.quanta.archetype.constant.RegAssert;
import com.quanta.archetype.exception.InvalidParameterException;

/**
 * 断言工具类
 *
 * @author Linine
 * @since 2022/11/19 21:15
 */
public class AssertUtils {

    public static void check(String data, RegAssert reqStr) {
        if (!data.matches(reqStr.getRegStr())) throw new InvalidParameterException();
    }

    public static boolean checkWithoutException(String data, RegAssert reqStr) {
        return data.matches(reqStr.getRegStr());
    }


}
