package com.jiawa.wiki.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

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

}
