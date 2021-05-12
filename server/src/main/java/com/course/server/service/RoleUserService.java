package com.course.server.service;

import com.course.server.domain.RoleUser;
import com.course.server.domain.RoleUserExample;
import com.course.server.dto.RoleUserDto;
import com.course.server.dto.PageDto;
import com.course.server.mapper.RoleUserMapper;
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
public class RoleUserService {
    @Resource
    private RoleUserMapper roleUserMapper;

    public void list( PageDto pageDto){
        PageHelper.startPage(pageDto.getPage(),pageDto.getSize());
        RoleUserExample roleUserExample = new RoleUserExample();
        List<RoleUser> roleUserList = roleUserMapper.selectByExample(roleUserExample);
        PageInfo<RoleUser> pageInfo = new PageInfo<>(roleUserList);//查询结果传给PageInfo
        pageDto.setTotal(pageInfo.getTotal());
        List<RoleUserDto> roleUserDtoList = CopyUtil.copyList(roleUserList, RoleUserDto.class);
        pageDto.setList(roleUserDtoList);


    }



     /**
     * roleUserDto看存不存在id 存在则修改 不存在就新建
     * 先复制一个不能动的    roleUser
     * @param roleUserDto
     */
    public void save(RoleUserDto roleUserDto){

        RoleUser roleUser = CopyUtil.copy(roleUserDto,RoleUser.class);
        if(StringUtils.isEmpty(roleUserDto.getId())){
            this.insert(roleUser);
        }else{
            this.update(roleUser);
        }

    }

    private void insert(RoleUser roleUser){
        roleUser.setId(UuidUtil.getShortUuid());
        roleUserMapper.insert(roleUser);
    }

    private void update(RoleUser roleUser){
        roleUserMapper.updateByPrimaryKey(roleUser);
    }

    public void delete(String id){
        roleUserMapper.deleteByPrimaryKey(id);
    }

}

