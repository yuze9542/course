package com.course.server.service;

import com.course.server.domain.Course;
import com.course.server.domain.CourseContent;
import com.course.server.domain.CourseExample;
import com.course.server.dto.*;
import com.course.server.mapper.CourseContentMapper;
import com.course.server.mapper.CourseMapper;
import com.course.server.mapper.my.MyCourseMapper;
import com.course.server.util.CopyUtil;
import com.course.server.util.UuidUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CourseService {
    private static final Logger Log = LoggerFactory.getLogger((CourseService.class));
    @Resource
    private CourseMapper courseMapper;
    @Resource
    private MyCourseMapper myCourseMapper;
    @Resource
    private CourseCategoryService courseCategoryService;
    @Resource
    private CourseContentMapper courseContentMapper;

    public void list( PageDto pageDto){
        //分页插件固定格式　１
        PageHelper.startPage(pageDto.getPage(),pageDto.getSize());
        //相当于 where 语句
        CourseExample courseExample = new CourseExample();
        courseExample.setOrderByClause("sort asc");
        //查找
        List<Course> courseList = courseMapper.selectByExample(courseExample);
        //分页插件固定格式　２ 将查找的东西放到pageinfo
        PageInfo<Course> pageInfo = new PageInfo<>(courseList);//查询结果传给PageInfo
        pageDto.setTotal(pageInfo.getTotal());
        List<CourseDto> courseDtoList =  CopyUtil.copyList(courseList, CourseDto.class);
        pageDto.setList(courseDtoList);
//        return pageDto;

    }
     /**
     * courseDto看存不存在id 存在则修改 不存在就新建
     * 先复制一个不能动的    course
     * @param courseDto
     */
    @Transactional
    public void save(CourseDto courseDto){

        Course course = CopyUtil.copy(courseDto,Course.class);
//        StringUtils.isEmpty
        if(StringUtils.isEmpty(courseDto.getId())){
            this.insert(course);
        }else{
            this.update(course);
        }
        //批量保存课程分类
        courseCategoryService.saveBatch(course.getId(),courseDto.getCategorys());
    }

    private void insert(Course course){
                Date now = new Date();
        course.setCreatedAt(now);
        course.setUpdatedAt(now);
        course.setId(UuidUtil.getShortUuid());
        courseMapper.insert(course);
    }

    private void update(Course course){
        course.setUpdatedAt(new Date());
        courseMapper.updateByPrimaryKey(course);
    }

    public void delete(String id){
        courseMapper.deleteByPrimaryKey(id);
    }

    public void updateTime(String courseId){
        Log.info("更新课程时长",courseId);
        myCourseMapper.updateTime(courseId);
    }

    /**
     * 查找课程内容
     * @param id
     * @return 课程内容Dto的值
     */
    public CourseContentDto findContent(String id){
        CourseContent courseContent = courseContentMapper.selectByPrimaryKey(id);
        if(courseContent == null){
            return null;
        }
        return CopyUtil.copy(courseContent,CourseContentDto.class);
    }

    public int saveContent(CourseContentDto courseContentDto){
        CourseContent courseContent =  CopyUtil.copy(courseContentDto,CourseContent.class);
        //直接更新 更新不到再插入
        int i = courseContentMapper.updateByPrimaryKeyWithBLOBs(courseContent);
        if(i==0){
            i = courseContentMapper.insert(courseContent);
        }
        return i;


    }

    /**
     * 排序
     * @param sortDto
     */
    @Transactional
    public void sort(SortDto sortDto) {
        // 修改当前记录的排序值
        myCourseMapper.updateSort(sortDto);

        // 如果排序值变大
        if (sortDto.getNewSort() > sortDto.getOldSort()) {
            myCourseMapper.moveSortForward(sortDto);
        }

        // 如果排序值变小
        if (sortDto.getNewSort() < sortDto.getOldSort()) {
            myCourseMapper.moveSortsBackward(sortDto);
        }
    }
}

