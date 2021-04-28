package com.example.redisdemo.controller;

import com.example.redisdemo.entity.Person;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁
 *
 * @author Castle
 * @Date 2021/4/28 11:41
 */
@RestController
public class LockController {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @GetMapping("/lock")
    public String updatePersonWithRedisLock(HttpSession session) throws InterruptedException {
        Person person = new Person();
        String uuid = UUID.randomUUID().toString();
        person.setId(session.getId());
        //分布式锁,如果存在为false，如果不存在，为true,同时设置过期时间
        Boolean lock = redisTemplate.opsForValue().setIfAbsent("person:lock:" + person.getId(), uuid, 30, TimeUnit.SECONDS);
        if (lock) {
            //todo
            String lockValue = redisTemplate.opsForValue().get("person:lock:" + person.getId());
            if (lockValue.equals(uuid)) {
                //删除自己的锁  但是操作不是原子性，可以使用lua脚本
                redisTemplate.delete("person:lock:" + person.getId());
            }
        } else {
            Thread.sleep(1000);
            updatePersonWithRedisLock(session);
        }

        return session.getId();
    }

}
