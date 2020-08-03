package com.course.server.service;

import com.course.server.domain.CourseContentFile;
import com.course.server.domain.CourseContentFileExample;
import com.course.server.dto.CourseContentFileDto;
import com.course.server.dto.PageDto;
import com.course.server.mapper.CourseContentFileMapper;
import com.course.server.util.CopyUtil;
import com.course.server.util.UuidUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class CourseContentFileService {
    @Resource
    private CourseContentFileMapper courseContentFileMapper;

    public List<CourseContentFileDto> list( String courseId){
        CourseContentFileExample courseContentFileExample = new CourseContentFileExample();
        CourseContentFileExample.Criteria criteria = courseContentFileExample.createCriteria();
        criteria.andCourseIdEqualTo(courseId);
        List<CourseContentFile> fileList = courseContentFileMapper.selectByExample(courseContentFileExample);
        return CopyUtil.copyList(fileList,CourseContentFileDto.class);
    }



     /**
     * courseContentFileDto看存不存在id 存在则修改 不存在就新建
     * 先复制一个不能动的    courseContentFile
     * @param courseContentFileDto
     */
    public void save(CourseContentFileDto courseContentFileDto){

        CourseContentFile courseContentFile = CopyUtil.copy(courseContentFileDto,CourseContentFile.class);
        if(StringUtils.isEmpty(courseContentFileDto.getId())){
            this.insert(courseContentFile);
        }else{
            this.update(courseContentFile);
        }

    }

    private void insert(CourseContentFile courseContentFile){
        courseContentFile.setId(UuidUtil.getShortUuid());
        courseContentFileMapper.insert(courseContentFile);
    }

    private void update(CourseContentFile courseContentFile){
        courseContentFileMapper.updateByPrimaryKey(courseContentFile);
    }

    public void delete(String id){
        courseContentFileMapper.deleteByPrimaryKey(id);
    }

}

