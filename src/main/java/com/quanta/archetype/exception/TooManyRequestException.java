package com.quanta.archetype.exception;

/**
 * 请求频繁异常
 * 可以配合令牌桶实现请求频繁拦截
 *
 * @author Linine
 * @since 2023/3/29 14:40
 */
public class TooManyRequestException extends RuntimeException {
    public TooManyRequestException() {
    }

    public TooManyRequestException(String message) {
        super(message);
    }
}
