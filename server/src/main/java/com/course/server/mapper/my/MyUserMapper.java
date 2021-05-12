package com.course.server.mapper.my;

import com.course.server.dto.ResourceDto;
import com.course.server.dto.SortDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MyUserMapper {
     List<ResourceDto> findResources(@Param("userId") String userId);

}
