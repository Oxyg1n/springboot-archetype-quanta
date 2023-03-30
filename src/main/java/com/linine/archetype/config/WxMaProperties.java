package com.linine.archetype.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


/**
 * 微信小程序配置
 *
 * @author Leslie Leung
 * @since 2021/10/16
 */
@Configuration
@ConfigurationProperties(prefix = "wx")
@Data
public class WxMaProperties {
    private String appId;
    private String appSecret;
    private boolean isDebug;
}
