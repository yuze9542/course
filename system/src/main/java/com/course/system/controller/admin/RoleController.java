package com.course.system.controller.admin;

import com.course.server.dto.RoleDto;
import com.course.server.dto.PageDto;
import com.course.server.dto.ResponseDto;
import com.course.server.exception.ValidatorException;
import com.course.server.service.RoleService;
import com.course.server.util.ValidatorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

//如果返回的是json格式的数据 用RestController
//如果返回的是页面 就是Controller
@RestController
@RequestMapping("/admin/role")
public class RoleController {
    private static final Logger log = LoggerFactory.getLogger(RoleController.class);
    public static final String BUSINESS_NAME = "角色";
    @Resource
    RoleService roleService ;

    @PostMapping("/list")
    public ResponseDto list(@RequestBody PageDto pageDto){
        ResponseDto responseDto = new ResponseDto();
       roleService.list(pageDto);
       responseDto.setContent(pageDto);
       return responseDto;
    }


    @PostMapping("/save")
    public ResponseDto save(@RequestBody RoleDto roleDto){
        // 保存校验
        ValidatorUtil.require(roleDto.getName(), "角色");
        ValidatorUtil.length(roleDto.getName(), "角色", 1, 50);
        ValidatorUtil.require(roleDto.getDesc(), "描述");
        ValidatorUtil.length(roleDto.getDesc(), "描述", 1, 100);

        ResponseDto responseDto = new ResponseDto();
        responseDto.setContent(roleDto);
        roleService.save(roleDto);
        return responseDto;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseDto delete(@PathVariable String id){
        ResponseDto responseDto = new ResponseDto();
        roleService.delete(id);
        return responseDto;
    }

    @PostMapping("/save-resource")
    public ResponseDto savaResource(@RequestBody RoleDto roleDto){
        log.info("保存角色资源关联");
        ResponseDto<RoleDto> responseDto = new ResponseDto<>();
        roleService.saveResource(roleDto);
        responseDto.setContent(roleDto);
        return responseDto;
    }

    /**
     * 加载已关联的资源
     */
    @GetMapping("/list-resource/{roleId}")
    public ResponseDto listResource(@PathVariable String roleId){
        log.info("加载角色资源关联");
        ResponseDto responseDto = new ResponseDto<>();
        List<String> resourceList = roleService.listResource(roleId);
        responseDto.setContent(resourceList);
        return responseDto;
    }

    /**
     * 加载用户
     */
    @GetMapping("/list-user/{roleId}")
    public ResponseDto listUser(@PathVariable String roleId){
        log.info("加载角色关联: {}",roleId);
        ResponseDto responseDto = new ResponseDto<>();
        List<String> userList = roleService.listUser(roleId);
        responseDto.setContent(userList);
        return responseDto;
    }
    /**
     * 保存用户
     */
    @PostMapping("/save-user")
    public ResponseDto saveUser(@RequestBody RoleDto roleDto){
        log.info("保存角色用户关联:  ",roleDto.getUserIds());
        ResponseDto<RoleDto> responseDto = new ResponseDto<>();
        roleService.saveUser(roleDto);
        responseDto.setContent(roleDto);
        return responseDto;
    }



}
