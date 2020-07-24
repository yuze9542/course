package com.course.system.controller;

import com.course.server.domain.NewTable;
import com.course.server.service.TestService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class TestController {

    @Resource
    TestService testService;

    @RequestMapping("/test")
    public List<NewTable> test(){
       return testService.list();
    }
}
