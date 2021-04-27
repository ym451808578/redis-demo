package com.example.redisdemo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Random;

/**
 * @author Castle
 * @Date 2021/4/27 15:37
 */
@RestController
@Slf4j
public class SecKillController {
    static String secKillScript ="local userid=KEYS[1];\r\n" +
            "local prodid=KEYS[2];\r\n" +
            "local qtkey='sk:'..prodid..\":qt\";\r\n" +
            "local usersKey='sk:'..prodid..\":usr\";\r\n" +
            "local userExists=redis.call(\"sismember\",usersKey,userid);\r\n" +
            "if tonumber(userExists)==1 then \r\n" +
            "   return 2;\r\n" +
            "end\r\n" +
            "local num= redis.call(\"get\" ,qtkey);\r\n" +
            "if tonumber(num)<=0 then \r\n" +
            "   return 0;\r\n" +
            "else \r\n" +
            "   redis.call(\"decr\",qtkey);\r\n" +
            "   redis.call(\"sadd\",usersKey,userid);\r\n" +
            "end\r\n" +
            "return 1" ;

    static String secKillScript2 =
            "local userExists=redis.call(\"sismember\",\"{sk}:0101:usr\",userid);\r\n" +
                    " return 1";
    @Resource
    private RedisTemplate redisTemplate;

    @GetMapping("/sec_kill")
    @Transactional(rollbackFor = Exception.class)
    public String secKillWithRedis() {
        String uid = String.valueOf(new Random().nextInt(50000));
        String prodId = "1001";
        //库存key
        String skKey = "sk:" + prodId + ":qt";
        //秒杀成功用户key
        String userKey = "sk:" + prodId + ":user";

        //监视库存key
        redisTemplate.watch(skKey);
        //获取库存，如果库存为null，表示未开始
        if (redisTemplate.opsForValue().get(skKey) == null) {
            log.info("秒杀未开始");
            return "秒杀未开始";
        }
        //判断用户是否重复秒杀
        if (redisTemplate.opsForSet().isMember(userKey, uid)) {
            log.info("每个用户只能秒杀一次");
            return "每个用户只能秒杀一次";
        }
        //如果库存数量小于1，秒杀结束
        if (Integer.valueOf(redisTemplate.opsForValue().get(skKey).toString()) <= 0) {
            log.info("秒杀结束");
            return "秒杀结束";
        }
        //开始秒杀
        redisTemplate.multi();
        redisTemplate.opsForValue().decrement(skKey);
        redisTemplate.opsForSet().add(userKey, uid);
        List<Object> results = redisTemplate.exec();
        log.info(results.toString());
        if (results.size()==0||results==null){
            log.info("秒杀失败");
            return "秒杀失败";
        }
        return "秒杀成功";
    }


}
