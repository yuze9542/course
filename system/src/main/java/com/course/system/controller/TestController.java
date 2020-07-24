package com.course.system.controller;

import com.course.system.domain.NewTable;
import com.course.system.service.TestService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

//如果返回的是json格式的数据 用RestController
//如果返回的是页面 就是Controller
@RestController
public class TestController {

    @Resource
    private TestService testService;


    @RequestMapping("/test")
    public List<NewTable> test(){
        System.out.println(testService.list());
        return testService.list();
    }
}
