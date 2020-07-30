package com.course.server.service;

import com.course.server.domain.CourseCategory;
import com.course.server.domain.CourseCategoryExample;
import com.course.server.dto.CategoryDto;
import com.course.server.dto.CourseCategoryDto;
import com.course.server.dto.PageDto;
import com.course.server.mapper.CourseCategoryMapper;
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
import java.util.List;

@Service
public class CourseCategoryService {
    @Resource
    private CourseCategoryMapper courseCategoryMapper;

    public void list( PageDto pageDto){
        PageHelper.startPage(pageDto.getPage(),pageDto.getSize());
        CourseCategoryExample courseCategoryExample = new CourseCategoryExample();
        List<CourseCategory> courseCategoryList = courseCategoryMapper.selectByExample(courseCategoryExample);
        PageInfo<CourseCategory> pageInfo = new PageInfo<>(courseCategoryList);//查询结果传给PageInfo
        pageDto.setTotal(pageInfo.getTotal());
        List<CourseCategoryDto> courseCategoryDtoList = CopyUtil.copyList(courseCategoryList, CourseCategoryDto.class);
        pageDto.setList(courseCategoryDtoList);


    }



     /**
     * courseCategoryDto看存不存在id 存在则修改 不存在就新建
     * 先复制一个不能动的    courseCategory
     * @param courseCategoryDto
     */
    public void save(CourseCategoryDto courseCategoryDto){

        CourseCategory courseCategory = CopyUtil.copy(courseCategoryDto,CourseCategory.class);
        if(StringUtils.isEmpty(courseCategoryDto.getId())){
            this.insert(courseCategory);
        }else{
            this.update(courseCategory);
        }

    }
    /**
     * 批量保存
     * @param
     */
    @Transactional
    public void saveBatch(String courseId, List<CategoryDto> categoryDtoList){
        CourseCategoryExample courseCategoryExample = new CourseCategoryExample();
        courseCategoryExample.createCriteria().andCourseIdEqualTo(courseId);
        courseCategoryMapper.deleteByExample(courseCategoryExample);
        for (int i = 0; i < categoryDtoList.size(); i++) {
            CategoryDto categoryDto = categoryDtoList.get(i);
            CourseCategory courseCategory = new CourseCategory();
            courseCategory.setId(UuidUtil.getShortUuid());
            courseCategory.setCategoryId(categoryDto.getId());
            courseCategory.setCourseId(courseId);
            insert(courseCategory);
        }
    }
    private void insert(CourseCategory courseCategory){
        courseCategory.setId(UuidUtil.getShortUuid());
        courseCategoryMapper.insert(courseCategory);
    }

    private void update(CourseCategory courseCategory){
        courseCategoryMapper.updateByPrimaryKey(courseCategory);
    }

    public void delete(String id){
        courseCategoryMapper.deleteByPrimaryKey(id);
    }

    /**
     * 查找课程下所有分类
     */
    public List<CourseCategoryDto> listByCourse(String courseId){
        CourseCategoryExample example = new CourseCategoryExample();
        example.createCriteria().andCourseIdEqualTo(courseId);
        List<CourseCategory> courseCategoryList = courseCategoryMapper.selectByExample(example);
        return CopyUtil.copyList(courseCategoryList,CourseCategoryDto.class);
    }
}

