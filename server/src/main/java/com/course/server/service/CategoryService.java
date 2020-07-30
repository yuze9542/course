package com.course.server.service;

import com.course.server.domain.Category;
import com.course.server.domain.CategoryExample;
import com.course.server.dto.CategoryDto;
import com.course.server.dto.PageDto;
import com.course.server.mapper.CategoryMapper;
import com.course.server.util.CopyUtil;
import com.course.server.util.UuidUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {
    @Resource
    private CategoryMapper categoryMapper;

    public void list( PageDto pageDto){
        PageHelper.startPage(pageDto.getPage(),pageDto.getSize());
        CategoryExample categoryExample = new CategoryExample();
        categoryExample.setOrderByClause("sort asc");
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);
        PageInfo<Category> pageInfo = new PageInfo<>(categoryList);//查询结果传给PageInfo
        pageDto.setTotal(pageInfo.getTotal());
        List<CategoryDto> categoryDtoList = CopyUtil.copyList(categoryList, CategoryDto.class);
        pageDto.setList(categoryDtoList);
    }

    public List<CategoryDto> all( ){
        CategoryExample categoryExample = new CategoryExample();
        categoryExample.setOrderByClause("sort asc");
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);
        List<CategoryDto> categoryDtoList = CopyUtil.copyList(categoryList, CategoryDto.class);
        return categoryDtoList;
    }



     /**
     * categoryDto看存不存在id 存在则修改 不存在就新建
     * 先复制一个不能动的    category
     * @param categoryDto
     */
    public void save(CategoryDto categoryDto){

        Category category = CopyUtil.copy(categoryDto,Category.class);
        if(StringUtils.isEmpty(categoryDto.getId())){
            this.insert(category);
        }else{
            this.update(category);
        }

    }

    private void insert(Category category){
        category.setId(UuidUtil.getShortUuid());
        categoryMapper.insert(category);
    }

    private void update(Category category){
        categoryMapper.updateByPrimaryKey(category);
    }

    @Transactional
    public void delete(String id){
        deleteChildren(id);
        categoryMapper.deleteByPrimaryKey(id);
    }

    private void deleteChildren(String id) {
        Category category = categoryMapper.selectByPrimaryKey(id);
        if("00000000".equals(category.getParent())){
            //如果是一级分类 需要删除旗下的二级分类
            CategoryExample example = new CategoryExample();
            example.createCriteria().andParentEqualTo(category.getId());
            categoryMapper.deleteByExample(example);
        }
    }

}

