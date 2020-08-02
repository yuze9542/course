package com.course.file.controller.admin;

import com.course.server.dto.FileDto;
import com.course.server.dto.ResponseDto;
import com.course.server.service.FileService;
import com.course.server.service.TestService;
import com.course.server.util.UuidUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class UploadController {

    @Value("${file.path}")
    private String FILE_PATH;
    @Value("${file.domain}")
    private String FILE_DOMAIN;
    @Resource
    private FileService fileService;

    //slf4j
    private static final Logger log = LoggerFactory.getLogger(UploadController.class);
    private static final String BUSINESS_NAME = "文件上传";

    @PostMapping("/upload")
    //file 必须要和前端名字一致 formData的key一致
    public ResponseDto upload(@RequestParam MultipartFile file) throws IOException {

        log.info("上传文件开始:");
        log.info("文件名: {}", file.getOriginalFilename());
        log.info("文件大小: {}",file.getSize());

        //保存文件到本地
        String filename = file.getOriginalFilename();
        String suffix = filename.substring(filename.indexOf(".")+1).toLowerCase(); //文件后缀
        String key = UuidUtil.getShortUuid();
        String path = "/teacher/"+key+"."+suffix;
        String fullpath = FILE_PATH +path;
        File dest = new File(fullpath);//找到文件夹 需要提前创建好
        file.transferTo(dest);//将文件写到全路径
        log.info(dest.getAbsolutePath());
        log.info("保存文件记录开始");
        FileDto fileDto = new FileDto();
        fileDto.setName(filename);
        fileDto.setPath(path);
        fileDto.setSize(Math.toIntExact(file.getSize()));
        fileDto.setSuffix(suffix);
        fileDto.setUse("");
        fileService.save(fileDto);

        ResponseDto responseDto = new ResponseDto();
        responseDto.setContent(FILE_DOMAIN+path);
        return responseDto;
    }

}
