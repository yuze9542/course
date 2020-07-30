package com.course.business.controller.admin;

import com.course.server.dto.*;
import com.course.server.exception.ValidatorException;
import com.course.server.service.CourseCategoryService;
import com.course.server.service.CourseService;
import com.course.server.util.ValidatorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

//如果返回的是json格式的数据 用RestController
//如果返回的是页面 就是Controller
@RestController
@RequestMapping("/admin/course")
public class CourseController {
//    private static final Logger LOG = LoggerFactory.getLogger(CourseController.class);
    public static final String BUSINESS_NAME = "课程";
    @Resource
    private CourseService courseService ;
    @Resource
    private CourseCategoryService courseCategoryService;

    @PostMapping("/list")
    public ResponseDto list(@RequestBody PageDto pageDto){
        ResponseDto responseDto = new ResponseDto();
       courseService.list(pageDto);
       responseDto.setContent(pageDto);
       return responseDto;
    }


    @PostMapping("/save")
    public ResponseDto save(@RequestBody CourseDto courseDto){
        // 保存校验
                ValidatorUtil.require(courseDto.getName(), "名称");
                ValidatorUtil.length(courseDto.getName(), "名称", 1, 50);
                ValidatorUtil.length(courseDto.getSummary(), "概述", 1, 2000);
                ValidatorUtil.length(courseDto.getImage(), "封面", 1, 100);

        ResponseDto responseDto = new ResponseDto();
        responseDto.setContent(courseDto);
        courseService.save(courseDto);
        return responseDto;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseDto delete(@PathVariable String id){
        ResponseDto responseDto = new ResponseDto();
        courseService.delete(id);
        return responseDto;
    }
    /**
     * 查找课程下所有分类
     * @param courseId
     */
    @PostMapping("/list-category/{courseId}")
    public ResponseDto listCategory(@PathVariable(value = "courseId") String courseId) {
        ResponseDto responseDto = new ResponseDto();
        List<CourseCategoryDto> dtoList = courseCategoryService.listByCourse(courseId);
        responseDto.setContent(dtoList);
        return responseDto;
    }

    /**
     * 查找课程下内容
     * @param
     * @PathVariable 是拼值(类似id)用的 后面要加value="id"获取
     * 而@RequestBody 是发送过来的内容
     */
    @PostMapping("/find-content/{id}")
    public ResponseDto findContent(@PathVariable(value = "id") String id) {
        ResponseDto responseDto = new ResponseDto();
        CourseContentDto courseContentDto = courseService.findContent(id);
        responseDto.setContent(courseContentDto);
        return responseDto;
    }

    /**
     * 保存课程下内容
     * @param
     */
    @PostMapping("/save-content")
    public ResponseDto saveContent(@RequestBody CourseContentDto courseContentDto) {
        ResponseDto responseDto = new ResponseDto();
        courseService.saveContent(courseContentDto);
//        responseDto.setContent(courseContentDto); //因为是从前端获取来的就不传过去了
        return responseDto;
    }
}
