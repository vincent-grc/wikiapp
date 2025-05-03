package com.jiawa.wiki.controller;

import com.jiawa.wiki.domain.Test;
import com.jiawa.wiki.mapper.TestMapper;
import com.jiawa.wiki.service.TestService;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

//Returns HTTP views.
//@Controller

//Returns data directly (e.g., JSON or XML), not views.
@RestController
public class TestController {
    private static final Logger LOG = LoggerFactory.getLogger(TestController.class);
    /*
    If the test.hello is not declared,
    it will use the value after the colon (DEFAULT)
     */
    @Value("${test.hello:DEFAULT}")
    private String testHello;

    @Autowired
    private TestService testService;

    @Resource
    private RedisTemplate redisTemplate;

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

    @RequestMapping("/redis/set/{key}/{value}")
    public String set(@PathVariable Long key, @PathVariable String value) {
        redisTemplate.opsForValue().set(key, value, 3600, TimeUnit.SECONDS);
        LOG.info("key: {}, value: {}", key, value);
        return "success";
    }

    @RequestMapping("/redis/get/{key}")
    public Object get(@PathVariable Long key) {
        Object object = redisTemplate.opsForValue().get(key);
        LOG.info("key: {}, value: {}", key, object);
        return object;
    }
}
