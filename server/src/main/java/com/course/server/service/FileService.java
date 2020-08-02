package com.course.server.service;

import com.course.server.domain.File;
import com.course.server.domain.FileExample;
import com.course.server.dto.FileDto;
import com.course.server.dto.PageDto;
import com.course.server.mapper.FileMapper;
import com.course.server.util.CopyUtil;
import com.course.server.util.UuidUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.util.ArrayList;
        import java.util.Date;
import java.util.List;

@Service
public class FileService {
    @Resource
    private FileMapper fileMapper;

    public void list( PageDto pageDto){
        PageHelper.startPage(pageDto.getPage(),pageDto.getSize());
        FileExample fileExample = new FileExample();
        List<File> fileList = fileMapper.selectByExample(fileExample);
        PageInfo<File> pageInfo = new PageInfo<>(fileList);//查询结果传给PageInfo
        pageDto.setTotal(pageInfo.getTotal());
        List<FileDto> fileDtoList = CopyUtil.copyList(fileList, FileDto.class);
        pageDto.setList(fileDtoList);


    }



     /**
     * fileDto看存不存在id 存在则修改 不存在就新建
     * 先复制一个不能动的    file
     * @param fileDto
     */
    public void save(FileDto fileDto){

        File file = CopyUtil.copy(fileDto,File.class);
        if(StringUtils.isEmpty(fileDto.getId())){
            this.insert(file);
        }else{
            this.update(file);
        }

    }

    private void insert(File file){
                Date now = new Date();
        file.setCreatedAt(now);
        file.setUpdatedAt(now);
        file.setId(UuidUtil.getShortUuid());
        fileMapper.insert(file);
    }

    private void update(File file){
        file.setUpdatedAt(new Date());
        fileMapper.updateByPrimaryKey(file);
    }

    public void delete(String id){
        fileMapper.deleteByPrimaryKey(id);
    }

}

