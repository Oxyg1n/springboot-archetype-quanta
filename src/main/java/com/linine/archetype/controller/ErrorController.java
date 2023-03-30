package com.linine.archetype.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;

/**
 * 错误页控制器
 *
 * @author Linine
 * @since 2023/3/29 15:12
 */
@Controller
public class ErrorController {


    /**
     * 跳转到错误页面
     * 举例场景：下载文件找不到本地资源
     * 示例： model.addAttribute("msg", e.getMessage()); return "forward:/file/error";
     *
     * @param model    model
     * @param response 响应
     * @return 错误页
     */
    @GetMapping("/error")
    public String downloadError(Model model, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        return "Stop";
    }
}
