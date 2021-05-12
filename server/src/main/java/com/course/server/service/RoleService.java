package com.course.server.service;

import com.course.server.domain.*;
import com.course.server.dto.RoleDto;
import com.course.server.dto.PageDto;
import com.course.server.mapper.RoleMapper;
import com.course.server.mapper.RoleResourceMapper;
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
public class RoleService {
    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RoleResourceMapper roleResourceMapper;

    @Resource
    private RoleUserMapper roleUserMapper;

    public void list( PageDto pageDto){
        // 设置开始页数和每页多少size
        PageHelper.startPage(pageDto.getPage(),pageDto.getSize());
        RoleExample roleExample = new RoleExample();
        List<Role> roleList = roleMapper.selectByExample(roleExample);
        PageInfo<Role> pageInfo = new PageInfo<>(roleList);//查询结果传给PageInfo
        pageDto.setTotal(pageInfo.getTotal());
        List<RoleDto> roleDtoList = CopyUtil.copyList(roleList, RoleDto.class);
        pageDto.setList(roleDtoList);


    }



     /**
     * roleDto看存不存在id 存在则修改 不存在就新建
     * 先复制一个不能动的    role
     * @param roleDto
     */
    public void save(RoleDto roleDto){

        Role role = CopyUtil.copy(roleDto,Role.class);
        if(StringUtils.isEmpty(roleDto.getId())){
            this.insert(role);
        }else{
            this.update(role);
        }

    }

    private void insert(Role role){
        role.setId(UuidUtil.getShortUuid());
        roleMapper.insert(role);
    }

    private void update(Role role){
        roleMapper.updateByPrimaryKey(role);
    }

    public void delete(String id){
        roleMapper.deleteByPrimaryKey(id);
    }

    public void saveResource(RoleDto roleDto) {
        String reloId = roleDto.getId();
        List<String> resourceIds = roleDto.getResourceIds();
        //先清空库中所有当前角色下记录
        RoleResourceExample example = new RoleResourceExample();
        example.createCriteria().andRoleIdEqualTo(reloId);
        roleResourceMapper.deleteByExample(example);
        //保存资源角色
        for (int i = 0; i < resourceIds.size(); i++) {
            RoleResource roleResource = new RoleResource();
            roleResource.setId(UuidUtil.getShortUuid());
            roleResource.setRoleId(reloId);
            roleResource.setResourceId(resourceIds.get(i));
            roleResourceMapper.insert(roleResource);
        }
    }

    public List<String> listResource(String roleId) {
        //先清空库中所有当前角色下记录
        RoleResourceExample example = new RoleResourceExample();
        example.createCriteria().andRoleIdEqualTo(roleId);
        //当前角色下所有资源
        List<RoleResource> roleResourceList = roleResourceMapper.selectByExample(example);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < roleResourceList.size(); i++) {
            list.add(roleResourceList.get(i).getResourceId());
        }
        return list;
    }

    public void saveUser(RoleDto roleDto) {
        String roleId = roleDto.getId();
        List<String> userIdList = roleDto.getUserIds();
        System.out.println("userIdList:"+userIdList);
        //先清空库中所有当前角色下记录
        RoleUserExample example = new RoleUserExample();
        example.createCriteria().andRoleIdEqualTo(roleId);
        roleUserMapper.deleteByExample(example);
        //保存角色用户
        for (int i = 0; i < userIdList.size(); i++) {
            RoleUser roleUser = new RoleUser();
            roleUser.setId(UuidUtil.getShortUuid());
            roleUser.setRoleId(roleId);
            roleUser.setUserId(userIdList.get(i));
            roleUserMapper.insert(roleUser);
        }
    }

    public List<String> listUser(String roleId) {
        RoleUserExample example = new RoleUserExample();
        example.createCriteria().andRoleIdEqualTo(roleId);
        List<RoleUser> roleUserList = roleUserMapper.selectByExample(example);
        List<String> userIdList = new ArrayList<>();
        for (int i = 0; i < roleUserList.size(); i++) {
            userIdList.add(roleUserList.get(i).getUserId());
        }
        return  userIdList;
    }
}

