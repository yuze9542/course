package com.course.business.controller.admin;

import com.course.server.dto.CategoryDto;
import com.course.server.dto.PageDto;
import com.course.server.dto.ResponseDto;
import com.course.server.exception.ValidatorException;
import com.course.server.service.CategoryService;
import com.course.server.util.ValidatorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

//如果返回的是json格式的数据 用RestController
//如果返回的是页面 就是Controller
@RestController
@RequestMapping("/admin/category")
public class CategoryController {
//    private static final Logger LOG = LoggerFactory.getLogger(CategoryController.class);
    public static final String BUSINESS_NAME = "分类";
    @Resource
    CategoryService categoryService ;



    @PostMapping("/list")
    public ResponseDto list(@RequestBody PageDto pageDto){
        ResponseDto responseDto = new ResponseDto();
        categoryService.list(pageDto);
        responseDto.setContent(pageDto);
        return responseDto;
    }

    @PostMapping("/all")
    public ResponseDto all(){
        ResponseDto responseDto = new ResponseDto();
        List<CategoryDto> categoryDtoList = categoryService.all();
        responseDto.setContent(categoryDtoList);
        return responseDto;
    }


    @PostMapping("/save")
    public ResponseDto save(@RequestBody CategoryDto categoryDto){
        // 保存校验
                ValidatorUtil.require(categoryDto.getParent(), "父id");
                ValidatorUtil.require(categoryDto.getName(), "名称");
                ValidatorUtil.length(categoryDto.getName(), "名称", 1, 50);

        ResponseDto responseDto = new ResponseDto();
        responseDto.setContent(categoryDto);
        categoryService.save(categoryDto);
        return responseDto;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseDto delete(@PathVariable String id){
        ResponseDto responseDto = new ResponseDto();
        categoryService.delete(id);
        return responseDto;
    }

}
