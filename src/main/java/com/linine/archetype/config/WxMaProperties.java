package com.linine.archetype.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


/**
 * @author Leslie Leung
 * @description
 * @date 2021/10/16
 */
@Configuration
@ConfigurationProperties(prefix = "wx")
@Data
public class WxMaProperties {
    private String appId;
    private String appSecret;
    private boolean isDebug;
}
