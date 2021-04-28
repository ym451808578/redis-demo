package com.example.redisdemo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @author Castle
 * @Date 2021/4/28 11:35
 */
@RestController
public class SessionController {

    @GetMapping("/session")
    public String session(HttpSession httpSession){
        return httpSession.getId();
    }
}
