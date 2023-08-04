package com.quanta.archetype.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 腾讯云COS配置
 *
 * @author Leslie Leung
 * @since 2021/11/2
 */
@Configuration
@ConfigurationProperties(prefix = "qcloud.cos")
@Data
public class COSProperties {
    private String secretId;
    private String secretKey;
    private String region;
    private String url;
    private String bucketName;
}
