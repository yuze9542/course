package com.course.system.controller.admin;

import com.course.server.dto.RoleUserDto;
import com.course.server.dto.PageDto;
import com.course.server.dto.ResponseDto;
import com.course.server.exception.ValidatorException;
import com.course.server.service.RoleUserService;
import com.course.server.util.ValidatorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

//如果返回的是json格式的数据 用RestController
//如果返回的是页面 就是Controller
@RestController
@RequestMapping("/admin/roleUser")
public class RoleUserController {
//    private static final Logger LOG = LoggerFactory.getLogger(RoleUserController.class);
    public static final String BUSINESS_NAME = "角色用户关联";
    @Resource
    RoleUserService roleUserService ;

    @PostMapping("/list")
    public ResponseDto list(@RequestBody PageDto pageDto){
        ResponseDto responseDto = new ResponseDto();
       roleUserService.list(pageDto);
       responseDto.setContent(pageDto);
       return responseDto;
    }


    @PostMapping("/save")
    public ResponseDto save(@RequestBody RoleUserDto roleUserDto){
        // 保存校验
                ValidatorUtil.require(roleUserDto.getRoleId(), "角色");
                ValidatorUtil.require(roleUserDto.getUserId(), "用户");

        ResponseDto responseDto = new ResponseDto();
        responseDto.setContent(roleUserDto);
        roleUserService.save(roleUserDto);
        return responseDto;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseDto delete(@PathVariable String id){
        ResponseDto responseDto = new ResponseDto();
        roleUserService.delete(id);
        return responseDto;
    }

}
