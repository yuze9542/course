package com.course.business.controller.admin;

import com.course.server.domain.Chapter;
import com.course.server.domain.NewTable;
import com.course.server.dto.ChapterDto;
import com.course.server.dto.PageDto;
import com.course.server.service.ChapterService;
import com.course.server.service.TestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

//如果返回的是json格式的数据 用RestController
//如果返回的是页面 就是Controller
@RestController
@RequestMapping("/admin/chapter")
public class ChapterController {
    private static final Logger LOG = LoggerFactory.getLogger(ChapterController.class);
    @Resource
    ChapterService chapterService ;

    @RequestMapping("/list")
    public PageDto list(@RequestBody PageDto pageDto){
        LOG.info("pageDto: {}",pageDto);
       chapterService.list(pageDto);
       //pageDto四个属性 前两个是前端传递进来的 传到service包chapterService的list函数中
       return pageDto;
    }
}
