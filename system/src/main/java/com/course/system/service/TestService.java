package com.course.system.service;

import com.course.system.domain.NewTable;
import com.course.system.mapper.TestMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("testService")
public class TestService {
    @Resource
    private TestMapper testMapper;

    public List<NewTable> list(){
        System.out.println(testMapper.list());
        return testMapper.list();
    }
}
