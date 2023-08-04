package com.quanta.archetype.exception;

/**
 * 自定义业务异常
 * 基本上服务层有问题就会抛出的异常，会在{@link GlobalExceptionHandler}统一处理
 * ！！！注意！！！
 * 无论如何不要在代码中catch Exception，可能会误捕获ApiException
 *
 * @author Leslie Leung
 * @since 2021/12/3
 */
public class ApiException extends RuntimeException {

    public ApiException() {
        super();
    }

    public ApiException(String message) {
        super(message);
    }
}
