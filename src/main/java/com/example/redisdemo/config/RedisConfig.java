package com.example.redisdemo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.Serializable;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Castle
 * @Date 2021/4/27 8:56
 */
@Configuration
@ConfigurationProperties(prefix = "caching")
public class RedisConfig {
    @Bean
    public RedisTemplate<String, Serializable> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        RedisTemplate<String, Serializable> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
        redisTemplate.setEnableTransactionSupport(true);
        return redisTemplate;
    }


    @Bean
    public RedisCacheManager redisCacheManager(RedisTemplate redisTemplate) {
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisTemplate.getConnectionFactory());
        //RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig().serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisTemplate.getValueSerializer()));
        //配置个性化缓存配置
        RedisCacheConfiguration redisCacheConfiguration = this.buildRedisCacheConfigurationWithTTL(redisTemplate, RedisCacheConfiguration.
                defaultCacheConfig().getTtl().getSeconds()); //默认的redis缓存配置

        return new RedisCacheManager(redisCacheWriter, redisCacheConfiguration, this.getRedisCacheConfiguration(redisTemplate));
    }

    /**
     * 缓存雪崩方案——自定义过期时间
     * 配置注入，key是缓存名称，value是缓存有效期
     */
    private Map<String, Long> ttlMap;

    public Map<String, Long> getTtlMap() {
        return ttlMap;
    }

    public void setTtlMap(Map<String, Long> ttlMap) {
        this.ttlMap = ttlMap;
    }

    /**
     * 缓存雪崩方案——自定义过期时间
     * 根据ttlMap的属性装配结果，从而个性化配置RedisCacheConfiguration
     *
     * @param redisTemplate
     * @return
     */
    private Map<String, RedisCacheConfiguration> getRedisCacheConfiguration(RedisTemplate redisTemplate) {
        Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = new HashMap<>();
        for (Map.Entry<String, Long> entry : ttlMap.entrySet()) {
            String cacheName = entry.getKey();
            Long ttl = entry.getValue();
            redisCacheConfigurationMap.put(cacheName, this.buildRedisCacheConfigurationWithTTL(redisTemplate, ttl));
        }
        return redisCacheConfigurationMap;
    }

    /**
     * 缓存雪崩方案——自定义过期时间
     * 根据传参构建缓存配置
     *
     * @param redisTemplate
     * @param ttl
     * @return
     */
    private RedisCacheConfiguration buildRedisCacheConfigurationWithTTL(RedisTemplate redisTemplate, Long ttl) {
        return RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisTemplate.getValueSerializer()))
                .entryTtl(Duration.ofSeconds(ttl));
    }
}
