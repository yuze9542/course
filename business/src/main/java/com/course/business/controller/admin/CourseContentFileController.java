package com.course.business.controller.admin;

import com.course.server.dto.CourseContentFileDto;
import com.course.server.dto.PageDto;
import com.course.server.dto.ResponseDto;
import com.course.server.exception.ValidatorException;
import com.course.server.service.CourseContentFileService;
import com.course.server.util.ValidatorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

//如果返回的是json格式的数据 用RestController
//如果返回的是页面 就是Controller
@RestController
@RequestMapping("/admin/course-content-file")
public class CourseContentFileController {
    private static final Logger LOG = LoggerFactory.getLogger(CourseContentFileController.class);
    public static final String BUSINESS_NAME = "课程内容文件";
    @Resource
    CourseContentFileService courseContentFileService ;

    @GetMapping("/list/{courseId}")
    public ResponseDto list(@PathVariable  String courseId){
       ResponseDto responseDto = new ResponseDto();
       List<CourseContentFileDto> Filelist =  courseContentFileService.list(courseId);
       responseDto.setContent(Filelist);
       return responseDto;
    }


    @PostMapping("/save")
    public ResponseDto save(@RequestBody CourseContentFileDto courseContentFileDto){
        // 保存校验
                ValidatorUtil.require(courseContentFileDto.getCourseId(), "课程id");
                ValidatorUtil.length(courseContentFileDto.getUrl(), "地址", 1, 100);
                ValidatorUtil.length(courseContentFileDto.getName(), "文件名", 1, 100);

        ResponseDto responseDto = new ResponseDto();
        responseDto.setContent(courseContentFileDto);
        courseContentFileService.save(courseContentFileDto);
        return responseDto;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseDto delete(@PathVariable String id){
        System.out.println("ssssss"+id);
        ResponseDto responseDto = new ResponseDto();
        courseContentFileService.delete(id);
        return responseDto;
    }

}
