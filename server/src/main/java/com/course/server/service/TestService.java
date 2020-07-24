package com.course.server.service;

import com.course.server.domain.NewTable;
import com.course.server.domain.NewTableExample;
import com.course.server.mapper.NewTableMapper;
import com.course.server.mapper.TestMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TestService {
    @Resource
    private NewTableMapper newTableMapper;

    public List<NewTable> list(){
        NewTableExample newTableExample = new NewTableExample();
        newTableExample.createCriteria().andIdEqualTo("1");  //真正的where条件
//        newTableExample.setOrderByClause("id desc");  //到处排列
        return newTableMapper.selectByExample(newTableExample);
    }
}
