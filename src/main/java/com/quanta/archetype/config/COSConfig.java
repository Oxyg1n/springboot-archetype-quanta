package com.quanta.archetype.config;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.region.Region;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 腾讯云COS对象存储配置
 *
 * @author Leslie Leung
 * @since 2021/11/3
 */
@Configuration
public class COSConfig {

    private final COSProperties COSProperties;

    public COSConfig(COSProperties COSProperties) {
        this.COSProperties = COSProperties;
    }

    @Bean
    public COSClient cosClient() {
        COSCredentials cred = new BasicCOSCredentials(COSProperties.getSecretId(), COSProperties.getSecretKey());
        Region region = new Region(COSProperties.getRegion());
        ClientConfig clientConfig = new ClientConfig(region);
        clientConfig.setHttpProtocol(HttpProtocol.https);
        return new COSClient(cred, clientConfig);
    }
}
