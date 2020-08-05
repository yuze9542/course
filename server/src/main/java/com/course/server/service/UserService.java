package com.course.server.service;

import com.course.server.dto.LoginUserDto;
import com.course.server.exception.BusinessException;
import com.course.server.exception.BusinessExceptionCode;
import com.course.server.domain.User;
import com.course.server.domain.UserExample;
import com.course.server.dto.UserDto;
import com.course.server.dto.PageDto;
import com.course.server.mapper.UserMapper;
import com.course.server.util.CopyUtil;
import com.course.server.util.UuidUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Resource
    private UserMapper userMapper;

    public void list( PageDto pageDto){
        PageHelper.startPage(pageDto.getPage(),pageDto.getSize());
        UserExample userExample = new UserExample();
        List<User> userList = userMapper.selectByExample(userExample);
        PageInfo<User> pageInfo = new PageInfo<>(userList);//查询结果传给PageInfo
        pageDto.setTotal(pageInfo.getTotal());
        List<UserDto> userDtoList = CopyUtil.copyList(userList, UserDto.class);
        pageDto.setList(userDtoList);


    }



     /**
     * userDto看存不存在id 存在则修改 不存在就新建
     * 先复制一个不能动的    user
     * @param userDto
     */
    public void save(UserDto userDto){

        User user = CopyUtil.copy(userDto,User.class);
        if(StringUtils.isEmpty(userDto.getId())){
            this.insert(user);
        }else{
            this.update(user);
        }

    }

    private void insert(User user){
        user.setId(UuidUtil.getShortUuid());
        User isExistUser = selectByLoginName(user.getLoginName());
        if(isExistUser == null){
            userMapper.insert(user);
        }else {
            throw  new BusinessException(BusinessExceptionCode.USER_LOGIN_NAME_EXIST);
        }
    }

    /**
     * 用于查询用户名是否存在
     * @param
     */
    private User selectByLoginName(String loginName){
        UserExample example = new UserExample();
        example.createCriteria().andLoginNameEqualTo(loginName);
        List<User> userList = userMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(userList)){
            return null;
        }else{
            return userList.get(0);
        }

    }

    private void update(User user){
        user.setPassword(null);
        userMapper.updateByPrimaryKeySelective(user);
    }

    public void delete(String id){
        userMapper.deleteByPrimaryKey(id);
    }

    /**
     * 重置密码
     * @param userDto
     * @return
     */
    public void setPassword(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setPassword(userDto.getPassword());
        userMapper.updateByPrimaryKeySelective(user);
    }

    public LoginUserDto login(UserDto userDto) {
        User user = selectByLoginName(userDto.getLoginName());
        if(user.equals(null)){
            log.info("用户名不存在");
            throw  new BusinessException(BusinessExceptionCode.LOGIN_USER_ERROR);

        }else {
            if(user.getPassword().equals(userDto.getPassword())){
                //登陆成功
                return CopyUtil.copy(user,LoginUserDto.class);

            }else{
                log.info("密码错了,原密码：{},新密码{}",user.getPassword(),userDto.getPassword());
                throw  new BusinessException(BusinessExceptionCode.LOGIN_USER_ERROR);
            }
        }
    }
}

