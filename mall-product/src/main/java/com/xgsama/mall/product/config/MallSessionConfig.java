package com.xgsama.mall.product.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

/**
 * MallSessionConfig
 *
 * @author : xgSama
 * @date : 2021/10/24 18:57:03
 */
@Configuration
public class MallSessionConfig {

    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer cookieSerializer = new DefaultCookieSerializer();
        // 明确的指定Cookie的作用域
        cookieSerializer.setDomainName("mall.com");
        cookieSerializer.setCookieName("MALLSESSION");
        return cookieSerializer;
    }

    /**
     * 自定义序列化机制
     * 这里方法名必须是：springSessionDefaultRedisSerializer
     */
    @Bean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
        return new GenericJackson2JsonRedisSerializer();
    }
}
