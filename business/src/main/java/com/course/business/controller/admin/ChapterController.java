package com.course.business.controller.admin;

import com.course.server.domain.Chapter;
import com.course.server.domain.NewTable;
import com.course.server.dto.ChapterDto;
import com.course.server.service.ChapterService;
import com.course.server.service.TestService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

//如果返回的是json格式的数据 用RestController
//如果返回的是页面 就是Controller
@RestController
@RequestMapping("/admin")
public class ChapterController {

    @Resource
    ChapterService chapterService ;

    @RequestMapping("/chapter")
    public List<ChapterDto> test(){
       return chapterService.list();
    }
}
