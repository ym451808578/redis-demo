package com.example.redisdemo.controller;

import com.example.redisdemo.entity.User;
import com.example.redisdemo.mapper.UserMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Castle
 * @Date 2021/4/28 8:27
 */
@RestController
public class UserController {

    public static final String CACHE_OBJECT = "user";
    public static final String CACHE_LIST_KEY = "\"list\"";
    @Resource
    private UserMapper userMapper;


    @Cacheable(value = CACHE_OBJECT, key = "#id",sync = true)  //sync=true, 应对缓存击穿
    @GetMapping("/user/{id}")
    public User getUser(@PathVariable("id") Long id) {
        return userMapper.selectById(id);
    }

    @Cacheable(value = CACHE_OBJECT, key = CACHE_LIST_KEY)
    @GetMapping("/user/list")
    public List<User> list() {
        return userMapper.selectList(null);
    }

    @CacheEvict(value = CACHE_OBJECT, key = CACHE_LIST_KEY)
    @GetMapping("/user/add")
    public String addUser() {
        User user = new User();
        user.setUname("new user");
        int id = userMapper.insert(user);
        return String.valueOf(id);
    }

    @CachePut(value = CACHE_OBJECT, key = "#id")
    @CacheEvict(value = CACHE_OBJECT, key = CACHE_LIST_KEY)
    @GetMapping("/user/update/{id}")
    public String updateUser(@PathVariable("id") Long id) {
        String uname = "new_user" + id;
        User user = new User();
        user.setUid(id);
        user.setUname(uname);
        userMapper.updateById(user);
        return "success";
    }

    @Caching(evict = {
            @CacheEvict(value = CACHE_OBJECT, key = "#id"),
            @CacheEvict(value = CACHE_OBJECT, key = CACHE_LIST_KEY)
    })
    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userMapper.deleteById(id);
        return "success";
    }
}
