package com.course.system.controller;

import com.course.server.domain.NewTable;
import com.course.server.mapper.TestMapper;
import com.course.server.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

//如果返回的是json格式的数据 用RestController
//如果返回的是页面 就是Controller
@RestController
public class TestController {

    @Resource
    TestService testService ;

    @RequestMapping("/test")
    public List<NewTable> test(){
       return testService.list();
    }
}
