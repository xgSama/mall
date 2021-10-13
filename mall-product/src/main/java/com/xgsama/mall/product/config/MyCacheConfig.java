package com.xgsama.mall.product.config;

import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;


/**
 * 1、每一个需要缓存的数据我们都来指定要放到那个名字的缓存。【缓存的分区(按照业务类型分)】
 * 2、@Cacheable 代表当前方法的结果需要缓存，如果缓存中有，方法都不用调用，如果缓存中没有，会调用方法。最后将方法的结果放入缓存
 * 3、默认行为
 * 3.1 如果缓存中有，方法不再调用
 * 3.2 key是默认生成的:缓存的名字::SimpleKey::[](自动生成key值)
 * 3.3 缓存的value值，默认使用jdk序列化机制，将序列化的数据存到redis中
 * 3.4 默认时间是 -1：
 * <p>
 * 自定义操作：key的生成
 * 1. 指定生成缓存的key：key属性指定，接收一个 SpEl
 * 2. 指定缓存的数据的存活时间:配置文档中修改存活时间 ttl
 * 3. 将数据保存为json格式: 自定义配置类 MyCacheManager
 * <p>
 * 4、Spring-Cache的不足之处：
 * 1）、读模式
 * 缓存穿透：查询一个null数据。解决方案：缓存空数据
 * 缓存击穿：大量并发进来同时查询一个正好过期的数据。解决方案：加锁 ? 默认是无加锁的;使用sync = true来解决击穿问题
 * 缓存雪崩：大量的key同时过期。解决：加随机时间。加上过期时间
 * 2)、写模式：（缓存与数据库一致）
 * 1）、读写加锁。
 * 2）、引入Canal,感知到MySQL的更新去更新Redis
 * 3）、读多写多，直接去数据库查询就行
 * <p>
 * 总结：
 * 常规数据（读多写少，即时性，一致性要求不高的数据，完全可以使用Spring-Cache）：写模式(只要缓存的数据有过期时间就足够了)
 * 特殊数据：特殊设计
 * <p>
 * 原理：
 * CacheManager(RedisCacheManager)->Cache(RedisCache)->Cache负责缓存的读写
 * <p>------------------------------------------------------------------------------------------------------------------
 * CacheAutoConfiguration -> RedisCacheConfiguration ->
 * 自动配置了RedisCacheManager -> 初始化所有缓存 -> 每个缓存决定使用什么配置
 * <p>
 * -> 如果RedisCacheConfiguration有就使用已有的，没有就用默认配置
 * -> 想改缓存的位置，只需要给容器中放一个RedisCacheConfiguration即可
 * -> 就会应用到当前RedisCacheManager管理的所有缓存分区中
 *
 * @author : xgSama
 * @date : 2021/10/13 22:00:18
 */
@EnableConfigurationProperties(CacheProperties.class)
@EnableCaching
@Configuration
public class MyCacheConfig {

    /**
     * 配置文件的配置没有用上
     * 1. 原来和配置文件绑定的配置类为：
     * . @ConfigurationProperties(prefix = "spring.cache")
     * . public class CacheProperties
     * <p>
     * 2. 要让他生效，要加上 @EnableConfigurationProperties(CacheProperties.class)
     */
    @Bean
    public RedisCacheConfiguration redisCacheConfiguration(CacheProperties cacheProperties) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();

        config = config.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()));
        config = config.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));

        CacheProperties.Redis redisProperties = cacheProperties.getRedis();
        //将配置文件中所有的配置都生效
        if (redisProperties.getTimeToLive() != null) {
            config = config.entryTtl(redisProperties.getTimeToLive());
        }
        if (redisProperties.getKeyPrefix() != null) {
            config = config.prefixKeysWith(redisProperties.getKeyPrefix());
        }
        if (!redisProperties.isCacheNullValues()) {
            config = config.disableCachingNullValues();
        }
        if (!redisProperties.isUseKeyPrefix()) {
            config = config.disableKeyPrefix();
        }
        return config;
    }
}