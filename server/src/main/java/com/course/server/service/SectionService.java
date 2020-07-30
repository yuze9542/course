package com.course.server.service;

import com.course.server.domain.Section;
import com.course.server.domain.SectionExample;
import com.course.server.dto.SectionDto;
import com.course.server.dto.PageDto;
import com.course.server.dto.SectionPageDto;
import com.course.server.mapper.SectionMapper;
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
public class SectionService {
    @Resource
    private SectionMapper sectionMapper;
    @Resource
    private CourseService courseService;

    public void list( SectionPageDto sectionPageDto){
        PageHelper.startPage(sectionPageDto.getPage(),sectionPageDto.getSize());
        SectionExample sectionExample = new SectionExample();
        SectionExample.Criteria criteria = sectionExample.createCriteria(); //criteria类似于where条件
        if(!StringUtils.isEmpty(sectionPageDto.getCourseId())){
            criteria.andCourseIdEqualTo(sectionPageDto.getCourseId());
        }
        if(!StringUtils.isEmpty(sectionPageDto.getChapterId())){
            criteria.andChapterIdEqualTo(sectionPageDto.getChapterId());
        }
        sectionExample.setOrderByClause("sort asc");
        List<Section> sectionList = sectionMapper.selectByExample(sectionExample);
        PageInfo<Section> pageInfo = new PageInfo<>(sectionList);//查询结果传给PageInfo
        sectionPageDto.setTotal(pageInfo.getTotal());
        List<SectionDto> sectionDtoList =  CopyUtil.copyList(sectionList,SectionDto.class);
        sectionPageDto.setList(sectionDtoList);
//        return pageDto;

    }



     /**
     * sectionDto看存不存在id 存在则修改 不存在就新建
     * 先复制一个不能动的    section
     * @param sectionDto
     */
     @Transactional //事物
    public void save(SectionDto sectionDto)  {

        Section section = CopyUtil.copy(sectionDto,Section.class);
        if(StringUtils.isEmpty(sectionDto.getId())){
            this.insert(section);
        }else{
            this.update(section);
        }

        courseService.updateTime(sectionDto.getCourseId());

    }

    private void insert(Section section){
        Date now = new Date();
        section.setCreatedAt(now);
        section.setUpdatedAt(now);
        section.setId(UuidUtil.getShortUuid());

        sectionMapper.insert(section);
    }

    private void update(Section section){
        section.setUpdatedAt(new Date());
        sectionMapper.updateByPrimaryKey(section);
    }

    public void delete(String id){
        sectionMapper.deleteByPrimaryKey(id);
    }

}

