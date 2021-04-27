package com.example.redisdemo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author Castle
 * @Date 2021/4/27 15:34
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisConnectTest {

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Test
    public void redisTemplateTest() {
        redisTemplate.opsForValue().set("k1", "v1");
        redisTemplate.opsForValue().set("k2", "v2");
        System.out.println(redisTemplate.opsForValue().get("k1"));
        System.out.println(redisTemplate.opsForValue().get("k2"));
    }
}
