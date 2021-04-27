package com.example.redisdemo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author 45180
 */
@Data
@NoArgsConstructor
public class Person implements Serializable {

    private String id;
    private String name;
    private Address address;
}
