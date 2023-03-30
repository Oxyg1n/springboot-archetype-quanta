package com.linine.archetype.utils;

import com.linine.archetype.exception.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

/**
 * 发送邮件工具类
 *
 * @author Leslie Leung
 * @since 2021/11/9
 */
@Component
public class MailUtils {

    private final JavaMailSender javaMailSender;

    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String mailFrom;

    public MailUtils(JavaMailSender javaMailSender, TemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    private static final String MAIL_TITLE = "项目名称";

    /**
     * 发送邮件基础方法
     *
     * @param recipients 收件人数组
     * @param title      邮件标题
     * @param content    邮件正文
     * @param isHtml     是否html格式
     */
    private void sendMail(String[] recipients, String title, String content, boolean isHtml) {
        MimeMessage mimeMailMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMailMessage, true, "utf-8");
            messageHelper.setFrom(mailFrom, MAIL_TITLE);
            messageHelper.setTo(recipients);
            messageHelper.setSubject(title);
            messageHelper.setText(content, isHtml);
            javaMailSender.send(mimeMailMessage);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new ApiException("邮件发送出错！");
        }
    }


    /**
     * 生成邮箱验证码，码长6位，字母与数字混合
     *
     * @return 6位验证码
     */
    public String getVerificationCode() {
        // 增大数字权重，去除部分相似字母
        final String CHARACTERS = "0123456789012345678901234567890123456789abcdefghijkmnopqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ";
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 6; i++) {
            sb.append(CHARACTERS.charAt((int) (Math.random() * (CHARACTERS.length()))));
        }
        return sb.toString();
    }

    @Async("taskExecutor")
    public void sendVerifyMail(String email, String code) {
        String[] emails = {email};
        // 调用templateEngine渲染页面
        Context context = new Context();
        context.setVariable("email", email);
        context.setVariable("code", code);
        String process = templateEngine.process("VerifyMail", context);
        sendMail(emails, MAIL_TITLE + "注册验证", process, true);
    }

    @Async("taskExecutor")
    public void sendResetMail(String email, String code) {
        String[] emails = {email};
        // 调用templateEngine渲染页面
        Context context = new Context();
        context.setVariable("email", email);
        context.setVariable("code", code);
        String process = templateEngine.process("ResetMail", context);
        sendMail(emails, MAIL_TITLE + "找回密码", process, true);
    }

    @Async("taskExecutor")
    public void sendChangeMail(String email, String code) {
        String[] emails = {email};
        // 调用templateEngine渲染页面
        Context context = new Context();
        context.setVariable("email", email);
        context.setVariable("code", code);
        String process = templateEngine.process("ChangeMail", context);
        sendMail(emails, MAIL_TITLE + "更改绑定邮箱", process, true);
    }
}
