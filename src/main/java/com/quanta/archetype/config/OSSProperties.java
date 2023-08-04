package com.quanta.archetype.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 阿里云OSS配置
 *
 * @author quantacenter
 * @since 2022/7/7
 */
@Configuration
@ConfigurationProperties(prefix = "aliyun.oss")
@Data
public class OSSProperties {
    private String accessKeyId;
    private String accessKeySecret;
    private String endPoint;
    private String bucketName;
    private String urlPrefix;
}
