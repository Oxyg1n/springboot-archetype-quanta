package com.linine.archetype.constants;

/**
 * 响应状态码
 *
 * @author Leslie Leung
 * modeify by linine
 * @since 2021/9/13
 */
public enum ResultCode {
    SUCCESS(0, "操作成功"),
    FAILED(400, "操作失败，请重试"),
    SERVER_ERROR(500, "服务器开小差了，请稍后再试"),
    UNAUTHORIZED(401, "登录状态已失效，请重新登录"),
    VALIDATE_FAILED(402, "参数校验失败，请检查请求参数"),
    FORBIDDEN(403, "访问被拒绝，您没有相关权限"),
    NOT_FOUND(404, "请求的资源不存在"),
    TOO_MANY_REQUESTS(429, "请求频率过高，请稍后再试");

    private final int code;
    private final String msg;

    ResultCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
