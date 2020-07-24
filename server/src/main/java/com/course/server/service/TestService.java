package com.course.server.service;

import com.course.server.domain.NewTable;
import com.course.server.mapper.TestMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TestService {
    @Resource
    private TestMapper testMapper;

    public List<NewTable> list(){
        System.out.println("111111111111111");
        return testMapper.list();
    }
}
