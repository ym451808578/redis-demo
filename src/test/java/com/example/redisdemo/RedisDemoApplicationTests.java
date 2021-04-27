package com.example.redisdemo;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
class RedisDemoApplicationTests {

    @Resource(name = "redisTemplate")
    private ValueOperations<String, Object> valueOperations;

    @Resource
    private RedisTemplate redisTemplate;

    @Test
    void PhoneCodeTest() {
        String phone = "13316097772";
        String codeInput="100512";
        String pKey = "user:" + phone;
        String cKey = "code:" + phone;
        if (valueOperations.get(pKey) == null) {
            System.out.println("今天首次获取验证码");
            valueOperations.set(pKey, 0, 24, TimeUnit.HOURS);
        }
        System.out.println("当前的pKey：" + valueOperations.get(pKey));
        if (Integer.valueOf(valueOperations.get(pKey).toString()) > 2) {
            System.out.println("今天已经获取过三次验证码了！");
            return;
        }
        String code = generateCode(6);
        //valueOperations.set(cKey, code, 120, TimeUnit.SECONDS);
        if (valueOperations.setIfAbsent(cKey, code, 120, TimeUnit.SECONDS)) {
            valueOperations.increment(pKey);
            System.out.println("当前第" + valueOperations.get(pKey) + "次获取验证码");
            System.out.println("获取的验证码为：" + valueOperations.get(cKey));
        } else {
            System.out.println("当前存在验证码：" + valueOperations.get(cKey));
        }
        if (codeInput.equals(valueOperations.get(cKey))){
            redisTemplate.expire(cKey,1,TimeUnit.MICROSECONDS);
            System.out.println("验证码已使用");
        }
        System.out.println(valueOperations.get(cKey));
    }

    public String generateCode(int len) {
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < len; i++) {
            code.append(new Random().nextInt(10));
        }
        return code.toString();
    }

}
