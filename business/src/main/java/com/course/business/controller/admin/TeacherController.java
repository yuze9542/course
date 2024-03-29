package com.course.business.controller.admin;

import com.course.server.dto.TeacherDto;
import com.course.server.dto.PageDto;
import com.course.server.dto.ResponseDto;
import com.course.server.exception.ValidatorException;
import com.course.server.service.TeacherService;
import com.course.server.util.ValidatorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

//如果返回的是json格式的数据 用RestController
//如果返回的是页面 就是Controller
@RestController
@RequestMapping("/admin/teacher")
public class TeacherController {
//    private static final Logger LOG = LoggerFactory.getLogger(TeacherController.class);
    public static final String BUSINESS_NAME = "讲师";
    @Resource
    TeacherService teacherService ;

    @PostMapping("/all")
    public ResponseDto all(){
        ResponseDto responseDto = new ResponseDto();
        List<TeacherDto> teacherDto = teacherService.all();
        responseDto.setContent(teacherDto);
        return responseDto;
    }

    @PostMapping("/list")
    public ResponseDto list(@RequestBody PageDto pageDto){
        ResponseDto responseDto = new ResponseDto();
       teacherService.list(pageDto);
       responseDto.setContent(pageDto);
       return responseDto;
    }


    @PostMapping("/save")
    public ResponseDto save(@RequestBody TeacherDto teacherDto){
        // 保存校验
                ValidatorUtil.require(teacherDto.getName(), "姓名");
                ValidatorUtil.length(teacherDto.getName(), "姓名", 1, 50);
                ValidatorUtil.length(teacherDto.getNickname(), "昵称", 1, 50);
                ValidatorUtil.length(teacherDto.getImage(), "头像", 1, 100);
                ValidatorUtil.length(teacherDto.getPosition(), "职位", 1, 50);
                ValidatorUtil.length(teacherDto.getMotto(), "座右铭", 1, 50);
                ValidatorUtil.length(teacherDto.getIntro(), "简介", 1, 500);

        ResponseDto responseDto = new ResponseDto();
        responseDto.setContent(teacherDto);
        teacherService.save(teacherDto);
        return responseDto;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseDto delete(@PathVariable String id){
        ResponseDto responseDto = new ResponseDto();
        teacherService.delete(id);
        return responseDto;
    }

}
