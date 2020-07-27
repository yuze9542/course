package com.course.server.service;

import com.course.server.domain.Section;
import com.course.server.domain.SectionExample;
import com.course.server.dto.SectionDto;
import com.course.server.dto.PageDto;
import com.course.server.mapper.SectionMapper;
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
public class SectionService {
    @Resource
    private SectionMapper sectionMapper;

    public void list( PageDto pageDto){
        PageHelper.startPage(pageDto.getPage(),pageDto.getSize());
        SectionExample sectionExample = new SectionExample();
        List<Section> sectionList = sectionMapper.selectByExample(sectionExample);
        PageInfo<Section> pageInfo = new PageInfo<>(sectionList);//查询结果传给PageInfo
        pageDto.setTotal(pageInfo.getTotal());
        List<SectionDto> sectionDtoList = new ArrayList<SectionDto>();
        for (int i = 0, l = sectionList.size();i<l; i++) {
            Section section = sectionList.get(i);
            SectionDto sectionDto = new SectionDto();
            BeanUtils.copyProperties(section,sectionDto);
            sectionDtoList.add(sectionDto);
        }
        pageDto.setList(sectionDtoList);
//        return pageDto;

    }


//    public void save(SectionDto sectionDto){
//        Section section = new Section();
//        sectionDto.setId(UuidUtil.getShortUuid());
//        BeanUtils.copyProperties(sectionDto,section);
//        sectionMapper.insert(section);
//    }

     /**
     * sectionDto看存不存在id 存在则修改 不存在就新建
     * 先复制一个不能动的    section
     * @param sectionDto
     */
    public void save(SectionDto sectionDto){

        Section section = CopyUtil.copy(sectionDto,Section.class);
        if(StringUtils.isEmpty(sectionDto.getId())){
            this.insert(section);
        }else{
            this.update(section);
        }

    }

    private void insert(Section section){
        section.setId(UuidUtil.getShortUuid());
        sectionMapper.insert(section);
    }

    private void update(Section section){
        sectionMapper.updateByPrimaryKey(section);
    }

    public void delete(String id){
        sectionMapper.deleteByPrimaryKey(id);
    }

}

