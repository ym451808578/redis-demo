package com.example.redisdemo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Castle
 * @Date 2021/4/28 8:06
 */
@Data
@NoArgsConstructor
@TableName("t_user")
public class User implements Serializable {
    @TableId
    private Long uid;
    private String uname;
}
