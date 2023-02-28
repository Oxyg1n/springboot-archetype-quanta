package com.linine.archetype.bean;

import com.linine.archetype.constants.ResultCode;
import lombok.Getter;

/**
 * 返回前端数据
 *
 * @author Leslie Leung
 * @modify LiNine
 * @date 2021/9/13
 */
@Getter
public class JsonResponse<T> {
    private int code;
    private String msg;
    private T data;

    protected JsonResponse() {
    }

    protected JsonResponse(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    protected JsonResponse(T data) {
        this.code = 0;
        this.msg = "操作成功";
        this.data = data;
    }

    protected JsonResponse(int code, String msg, T data) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    public static <T> JsonResponse<T> success() {
        return new JsonResponse<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMsg());
    }

    public static <T> JsonResponse<T> success(String msg) {
        return new JsonResponse<>(ResultCode.SUCCESS.getCode(), msg);
    }

    public static <T> JsonResponse<T> success(T data) {
        return new JsonResponse<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMsg(), data);
    }

    public static <T> JsonResponse<T> success(T data, String msg) {
        return new JsonResponse<>(ResultCode.SUCCESS.getCode(), msg, data);
    }

    public static JsonResponse<Object> fail() {
        return new JsonResponse<>(ResultCode.FAILED.getCode(), ResultCode.FAILED.getMsg());
    }

    public static JsonResponse<Object> fail(String msg) {
        return new JsonResponse<>(ResultCode.FAILED.getCode(), msg);
    }

    // 系统异常
    public static JsonResponse<Object> error() {
        return new JsonResponse<>(ResultCode.SERVER_ERROR.getCode(), ResultCode.SERVER_ERROR.getMsg());
    }

    // 系统异常
    public static JsonResponse<Object> error(String msg) {
        return new JsonResponse<>(ResultCode.SERVER_ERROR.getCode(), msg);
    }

    // token异常
    public static JsonResponse<Object> tokenError() {
        return new JsonResponse<>(ResultCode.UNAUTHORIZED.getCode(), ResultCode.UNAUTHORIZED.getMsg());
    }

    // token异常
    public static JsonResponse<Object> tokenError(String msg) {
        return new JsonResponse<>(ResultCode.UNAUTHORIZED.getCode(), msg);
    }

    // token异常返回数据
    public static <T> JsonResponse<T> unauthorized(T data) {
        return new JsonResponse<>(ResultCode.UNAUTHORIZED.getCode(), ResultCode.UNAUTHORIZED.getMsg(), data);
    }

    // 参数验证异常
    public static JsonResponse<Object> paramError() {
        return new JsonResponse<>(ResultCode.VALIDATE_FAILED.getCode(), ResultCode.VALIDATE_FAILED.getMsg());
    }

    // 参数验证异常
    public static JsonResponse<Object> paramError(String msg) {
        return new JsonResponse<>(ResultCode.VALIDATE_FAILED.getCode(), msg);
    }

    // 权限异常
    public static JsonResponse<Object> permissionError() {
        return new JsonResponse<>(ResultCode.FORBIDDEN.getCode(), ResultCode.FORBIDDEN.getMsg());
    }

    // 权限异常
    public static JsonResponse<Object> permissionError(String msg) {
        return new JsonResponse<>(ResultCode.FORBIDDEN.getCode(), msg);
    }

    // 权限异常返回数据
    public static <T> JsonResponse<T> forbidden(T data) {
        return new JsonResponse<>(ResultCode.FORBIDDEN.getCode(), ResultCode.FORBIDDEN.getMsg(), data);
    }

}
