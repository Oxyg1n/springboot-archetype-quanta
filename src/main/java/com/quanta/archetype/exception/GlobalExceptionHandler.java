package com.quanta.archetype.exception;

import com.alibaba.fastjson.JSON;
import com.quanta.archetype.bean.JsonResponse;
import com.quanta.archetype.util.WechatBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 全局异常处理器
 *
 * @author Leslie Leung
 * @author Linine
 * @since 2021/12/5
 */
@Slf4j
@Order(0)
@ControllerAdvice(basePackages = "com.quanta.archetype.controller")
public class GlobalExceptionHandler {

    private static final String ERROR_MESSAGE_TEMPLATE = "%s\n[Msg]%s\n[File]%s\n[LogId]%s\n[Url]%s\n[Ip]%s\n[Args]%s\n[Token]%s";

    @Value("${project.isDebug}")
    private boolean isDebug;

    @ExceptionHandler(Exception.class)
    public @ResponseBody
    JsonResponse<Object> errorResult(Exception e) throws IOException {
        log.error(e.getMessage());
        if (isDebug) {
            // 本地调试只输出错误信息，并返回前端详细信息
            e.printStackTrace();
            return JsonResponse.systemError(e.getMessage());
        }
        // 发送错误信息到微信机器人
        WechatBot.send(createErrorMessage(e));
        // 线上环境仅返回系统错误
        return JsonResponse.systemError();
    }

    // 数据校验异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public @ResponseBody JsonResponse<Object> validationErrorResult(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        StringBuffer stringBuffer = new StringBuffer();
        if (result.getFieldErrorCount() > 0) {
            List<FieldError> fieldErrors = result.getFieldErrors();
            for (int i = 0; i < fieldErrors.size(); i++) {
                stringBuffer.append(fieldErrors.get(i).getDefaultMessage());
                if (i != fieldErrors.size() - 1) stringBuffer.append(',');
            }
        }
        return JsonResponse.paramError(String.format("参数校验错误[%s]", stringBuffer));
    }

    // 数据校验异常
    @ExceptionHandler(ValidationException.class)
    public @ResponseBody JsonResponse<Object> validationErrorResult(ValidationException e) {
        return JsonResponse.paramError(String.format("参数校验错误[%s]", e.getMessage()));
    }

    // 自定义参数参数校验异常
    @ExceptionHandler(InvalidParameterException.class)
    public @ResponseBody JsonResponse<Object> InvalidParameterResult(InvalidParameterException e) {
        if (e.getMessage() != null) return JsonResponse.paramError(e.getMessage());
        return JsonResponse.paramError();
    }

    // 查看资源权限错误异常
    @ExceptionHandler(PermissionDeniedException.class)
    public @ResponseBody JsonResponse<Object> permissionDeniedResult() {
        return JsonResponse.forbidden();
    }

    // 资源不存在异常
    @ExceptionHandler(RecordNotFoundException.class)
    public @ResponseBody JsonResponse<Object> recordNotFoundResult() {
        return JsonResponse.notFound();
    }

    // 请求限流异常
    @ExceptionHandler(TooManyRequestException.class)
    public @ResponseBody JsonResponse<Object> tooManyRequestResult() {
        return JsonResponse.tooManyRequests();
    }

    // 自定义异常处理类
    @ExceptionHandler(ApiException.class)
    public @ResponseBody
    JsonResponse<Object> apiErrorResult(ApiException e) {
        return JsonResponse.fail(e.getMessage());
    }

    // 微信机器人错误信息生成
    private String createErrorMessage(Exception e) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();
        String logId = (String) request.getAttribute("requestId");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String currentDate = formatter.format(date);
        String msg = String.format(ERROR_MESSAGE_TEMPLATE,
                currentDate,
                e.getMessage(),
                e.getStackTrace()[0].getFileName() + "." + e.getStackTrace()[0].getMethodName() + " (line: " + e.getStackTrace()[0].getLineNumber() + ")",
                logId,
                request.getRequestURL().toString(),
                request.getHeader("X-Real-IP"),
                request.getAttribute("args"),
                request.getHeader("Token"));
        Map<String, Object> req = new HashMap<>();
        req.put("msgtype", "text");
        Map<String, String> content = new HashMap<>();
        content.put("content", msg);
        req.put("text", content);
        return JSON.toJSONString(req);
    }
}
