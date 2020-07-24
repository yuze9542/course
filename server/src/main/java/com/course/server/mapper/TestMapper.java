package com.course.server.mapper;

import com.course.server.domain.NewTable;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface TestMapper {
    public List<NewTable> list();
}
