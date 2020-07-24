package com.course.server.mapper;

import com.course.server.domain.NewTable;
import com.course.server.domain.NewTableExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface NewTableMapper {
    long countByExample(NewTableExample example);

    int deleteByExample(NewTableExample example);

    int deleteByPrimaryKey(String id);

    int insert(NewTable record);

    int insertSelective(NewTable record);

    List<NewTable> selectByExample(NewTableExample example);

    NewTable selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") NewTable record, @Param("example") NewTableExample example);

    int updateByExample(@Param("record") NewTable record, @Param("example") NewTableExample example);

    int updateByPrimaryKeySelective(NewTable record);

    int updateByPrimaryKey(NewTable record);
}