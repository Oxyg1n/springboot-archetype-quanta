package com.quanta.archetype.config;

import com.quanta.archetype.util.MinioUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * （可选）Minio对象存储配置
 *
 * @author Linine
 * @since 2023/3/30 21:37
 */
@Configuration
@Slf4j
public class MinioConfig {

    @Value("${minio.url}")
    private String url;
    @Value("${minio.bucketName}")
    private String bucketName;
    @Value("${minio.accessKey}")
    private String accessKey;
    @Value("${minio.secretKey}")
    private String secretKey;

    @Bean
    public MinioUtils creatMinioClient() {
        return new MinioUtils(url, bucketName, accessKey, secretKey);
    }

}
