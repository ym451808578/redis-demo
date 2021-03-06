package com.example.redisdemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @author 45180
 */
@EnableCaching
@SpringBootApplication
@MapperScan("com.example.redisdemo.mapper")
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 1800)
public class RedisDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisDemoApplication.class, args);
    }

}

