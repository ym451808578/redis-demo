package com.example.redisdemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.redisdemo.entity.User;
import org.springframework.stereotype.Repository;

/**
 * @author Castle
 * @Date 2021/4/28 8:07
 */
@Repository
public interface UserMapper extends BaseMapper<User> {
}
