package com.course.business.controller.admin;

import com.course.server.domain.Chapter;
import com.course.server.dto.ChapterDto;
import com.course.server.dto.ChapterPageDto;
import com.course.server.dto.PageDto;
import com.course.server.dto.ResponseDto;
import com.course.server.exception.ValidatorException;
import com.course.server.service.ChapterService;
import com.course.server.util.ValidatorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

//如果返回的是json格式的数据 用RestController
//如果返回的是页面 就是Controller
@RestController
@RequestMapping("/admin/chapter")
public class ChapterController {
    private static final Logger log = LoggerFactory.getLogger(ChapterController.class);
    public static final String BUSINESS_NAME = "大章";
    @Resource
    ChapterService chapterService ;

    @PostMapping("/list")
    public ResponseDto list(@RequestBody ChapterPageDto chapterPageDto){
        log.info("列表查询");
        ResponseDto responseDto = new ResponseDto();
        ValidatorUtil.require(chapterPageDto.getCourseId(),"课程ID");
       chapterService.list(chapterPageDto);
       responseDto.setContent(chapterPageDto);
       //pageDto四个属性 前两个是前端传递进来的 传到service包chapterService的list函数中
       return responseDto;
    }


    @PostMapping("/save")
    public ResponseDto save(@RequestBody ChapterDto chapterDto){


        ValidatorUtil.require(chapterDto.getName(),"名称");
        ValidatorUtil.require(chapterDto.getCourseId(),"课程Id");
        ValidatorUtil.length(chapterDto.getCourseId(),"课程Id",1,8);

        ResponseDto responseDto = new ResponseDto();
        responseDto.setContent(chapterDto);
        chapterService.save(chapterDto);
        return responseDto;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseDto delete(@PathVariable String id){
        ResponseDto responseDto = new ResponseDto();
        chapterService.delete(id);
        return responseDto;
    }

}
