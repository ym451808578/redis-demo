package com.example.redisdemo;

import com.example.redisdemo.entity.Address;
import com.example.redisdemo.entity.Person;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTemplateTest {

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Resource
    private RedisTemplate<String, Person> redisTemplate;

    @Test
    public void redisTemplateTest(){
        Person person=new Person();
        person.setId("id1");
        person.setName("castle");
        Address address=new Address();
        address.setCountry("cn");
        address.setCity("gz");
        person.setAddress(address);
        stringRedisTemplate.opsForValue().set("person:str","castle",20, TimeUnit.SECONDS);
        redisTemplate.opsForValue().set("person:to",person,20,TimeUnit.SECONDS);
        System.out.println(stringRedisTemplate.opsForValue().get("person:str"));
        System.out.println(stringRedisTemplate.opsForValue().get("person:to"));
        //SetOperation
        Person person2=new Person();
        person2.setId("id2");
        person2.setName("ym");
        redisTemplate.opsForSet().add("personSet",person,person2);
        Set<Person> result=redisTemplate.opsForSet().members("personSet");
        System.out.println(result);
        //HashOperation
        redisTemplate.opsForHash().put("hash:person","id",person.getId());
        redisTemplate.opsForHash().put("hash:person","name",person.getName());
        redisTemplate.opsForHash().put("hash:person","address",person.getAddress());
        System.out.println(redisTemplate.opsForHash().get("hash:person","address"));
        //listOperation
        redisTemplate.opsForList().leftPush("list:person",person);
        redisTemplate.opsForList().leftPush("list:person",person2);
        System.out.println(redisTemplate.opsForList().rightPop("list:person"));
    }
}
