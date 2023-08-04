package com.quanta.archetype.util;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

/**
 * 企业微信通知机器人
 *
 * @author Leslie Leung
 * @since 2021/12/5
 */
public class WechatBot {

    private WechatBot() {
        throw new IllegalStateException("Utility class");
    }

    public static final String WEBHOOK_URL = "";

    public static void send(String msg) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(WEBHOOK_URL);
            httpPost.addHeader("Content-Type", "application/json; charset=utf-8");
            StringEntity stringEntity = new StringEntity(msg, "utf-8");
            httpPost.setEntity(stringEntity);
            httpClient.execute(httpPost);
        }
    }
}
