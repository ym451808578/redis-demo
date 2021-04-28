package com.example.redisdemo;

import com.example.redisdemo.entity.User;
import com.example.redisdemo.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author Castle
 * @Date 2021/4/28 8:08
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CacheTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void userTest() {
        List<User> users = userMapper.selectList(null);
        System.out.println(users);
        User user=userMapper.selectById(1L);
        System.out.println(user);
    }
}
