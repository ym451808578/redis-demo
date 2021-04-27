package com.example.redisdemo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author 45180
 */
@Data
@NoArgsConstructor
public class Address implements Serializable {
    private String country;
    private String city;
}
