package com.quanta.archetype.config;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 微信小程序服务
 *
 * @author Leslie Leung
 * @since 2021/10/23
 */
@Configuration
@ConditionalOnClass(WxMaService.class)
public class WxMaConfig {


    private final WxMaProperties wxMaProperties;

    public WxMaConfig(WxMaProperties wxMaProperties) {
        this.wxMaProperties = wxMaProperties;
    }

    @Bean
    @ConditionalOnMissingBean
    public WxMaService wxMaService() {
        WxMaDefaultConfigImpl config = new WxMaDefaultConfigImpl();
        config.setAppid(wxMaProperties.getAppId());
        config.setSecret(wxMaProperties.getAppSecret());
        WxMaService service = new WxMaServiceImpl();
        service.setWxMaConfig(config);
        return service;
    }
}
