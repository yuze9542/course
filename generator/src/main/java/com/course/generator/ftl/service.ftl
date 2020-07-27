package com.course.server.service;

import com.course.server.domain.${Domain};
import com.course.server.domain.${Domain}Example;
import com.course.server.dto.${Domain}Dto;
import com.course.server.dto.PageDto;
import com.course.server.mapper.${Domain}Mapper;
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
public class ${Domain}Service {
    @Resource
    private ${Domain}Mapper ${domain}Mapper;

    public void list( PageDto pageDto){
        PageHelper.startPage(pageDto.getPage(),pageDto.getSize());
        ${Domain}Example ${domain}Example = new ${Domain}Example();
        List<${Domain}> ${domain}List = ${domain}Mapper.selectByExample(${domain}Example);
        PageInfo<${Domain}> pageInfo = new PageInfo<>(${domain}List);//查询结果传给PageInfo
        pageDto.setTotal(pageInfo.getTotal());
        List<${Domain}Dto> ${domain}DtoList = new ArrayList<${Domain}Dto>();
        for (int i = 0, l = ${domain}List.size();i<l; i++) {
            ${Domain} ${domain} = ${domain}List.get(i);
            ${Domain}Dto ${domain}Dto = new ${Domain}Dto();
            BeanUtils.copyProperties(${domain},${domain}Dto);
            ${domain}DtoList.add(${domain}Dto);
        }
        pageDto.setList(${domain}DtoList);
//        return pageDto;

    }


//    public void save(${Domain}Dto ${domain}Dto){
//        ${Domain} ${domain} = new ${Domain}();
//        ${domain}Dto.setId(UuidUtil.getShortUuid());
//        BeanUtils.copyProperties(${domain}Dto,${domain});
//        ${domain}Mapper.insert(${domain});
//    }

     /**
     * ${domain}Dto看存不存在id 存在则修改 不存在就新建
     * 先复制一个不能动的    ${domain}
     * @param ${domain}Dto
     */
    public void save(${Domain}Dto ${domain}Dto){

        ${Domain} ${domain} = CopyUtil.copy(${domain}Dto,${Domain}.class);
        if(StringUtils.isEmpty(${domain}Dto.getId())){
            this.insert(${domain});
        }else{
            this.update(${domain});
        }

    }

    private void insert(${Domain} ${domain}){
        ${domain}.setId(UuidUtil.getShortUuid());
        ${domain}Mapper.insert(${domain});
    }

    private void update(${Domain} ${domain}){
        ${domain}Mapper.updateByPrimaryKey(${domain});
    }

    public void delete(String id){
        ${domain}Mapper.deleteByPrimaryKey(id);
    }

}

