package com.jiawa.wiki.controller;

import org.springframework.web.bind.annotation.*;

//Returns HTTP views.
//@Controller

//Returns data directly (e.g., JSON or XML), not views.
@RestController
public class TestController {

    /*
    Request handles all kinds of requests.
    @RequestMapping
    @RequestMapping(value="/hello", method = RequestMethod.GET)
     */
    @GetMapping("/hello")
    public String hello() {
        return "Hello World";
    }

    @PostMapping("/hello/post")
    public String helloPost(String name) {
        return "Hello post! " + name;
    }

}
