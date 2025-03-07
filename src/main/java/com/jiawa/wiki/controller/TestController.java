package com.jiawa.wiki.controller;

import com.jiawa.wiki.domain.Test;
import com.jiawa.wiki.mapper.TestMapper;
import com.jiawa.wiki.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//Returns HTTP views.
//@Controller

//Returns data directly (e.g., JSON or XML), not views.
@RestController
public class TestController {
    /*
    If the test.hello is not declared,
    it will use the value after the colon (DEFAULT)
     */
    @Value("${test.hello:DEFAULT}")
    private String testHello;

    @Autowired
    private TestService testService;

    /*
    Request handles all kinds of requests.
    @RequestMapping
    @RequestMapping(value="/hello", method = RequestMethod.GET)
     */
    @GetMapping("/hello")
    public String hello() {
        return testHello;
    }

    @PostMapping("/hello/post")
    public String helloPost(String name) {
        return "Hello post! " + name;
    }

    @GetMapping("/test/list")
    public List<Test> testList() {
        return testService.list();
    }
}
