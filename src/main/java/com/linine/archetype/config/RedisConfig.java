package com.linine.archetype.config;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * redis配置类 序列化
 *
 * @author Linine
 * @since 2023/2/28 20:26
 */
@Configuration
public class RedisConfig {
    @Bean("redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        // String的序列化
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // fastjson序列化
        GenericFastJsonRedisSerializer serializer = new GenericFastJsonRedisSerializer();
        // key采用String的序列化方式
        template.setKeySerializer(stringRedisSerializer);
        // hash的key也采用String的序列化方式
        template.setHashKeySerializer(stringRedisSerializer);
        // value的序列化采用fastjson
        template.setValueSerializer(serializer);
        // hash的value采用fastjson的序列化
        template.setHashValueSerializer(serializer);
        return template;

    }

}
