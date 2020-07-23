package com.course.system.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//如果返回的是json格式的数据 用RestController
//如果返回的是页面 就是Controller
@RestController
public class TestController {

    @RequestMapping("/test")
    public String test(){
        return "success";
    }
}
