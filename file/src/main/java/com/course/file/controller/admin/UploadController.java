package com.course.file.controller.admin;

import com.course.server.dto.FileDto;
import com.course.server.dto.ResponseDto;
import com.course.server.enums.FileUseEnum;
import com.course.server.service.FileService;
import com.course.server.service.TestService;
import com.course.server.util.UuidUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
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
    public ResponseDto upload(@RequestParam MultipartFile file,String use) throws IOException {

        log.info("上传文件开始:");
        log.info("文件名: {}", file.getOriginalFilename());
        log.info("文件大小: {}",file.getSize());

        //保存文件到本地
        FileUseEnum useEnum = FileUseEnum.getByCode(use);
        String filename = file.getOriginalFilename();
        String suffix = filename.substring(filename.indexOf(".")+1).toLowerCase(); //文件后缀

        //如果不存在则创建
        String dir = useEnum.name().toLowerCase();
        File fullDir = new File(FILE_PATH+dir);
        if(!fullDir.exists())
            fullDir.mkdir();

        String key = UuidUtil.getShortUuid();
        //File.separator是产生斜杠的意思
        String path = dir + File.separator + key + "." + suffix;
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
        fileDto.setPath(FILE_DOMAIN+path);
        responseDto.setContent(fileDto);
        return responseDto;
    }

    @GetMapping("/merge")
    public ResponseDto merge() throws IOException {
        File newFile = new File(FILE_PATH + "/course/test123.mp4");   //最终输出的文件
        FileOutputStream outputStream = new FileOutputStream(newFile, true); //文件追加写入
        FileInputStream fileInputStream = null; //分片文件
        byte[] byt = new byte[1 * 1024 * 1024];
        int len;
        try {
            //读取第一个分片 b180UX5L.blob
            fileInputStream = new FileInputStream(new File(FILE_PATH + "/course/b180UX5L.blob"));
            while ((len = fileInputStream.read(byt)) != -1) {
                outputStream.write(byt, 0, len);
            }
            //读取第二个分片 vE8DbTCI.blob
            fileInputStream = new FileInputStream(new File(FILE_PATH + "/course/b180UX5L.blob"));
            while ((len = fileInputStream.read(byt)) != -1) {
                outputStream.write(byt, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ResponseDto responseDto = new ResponseDto();
        return responseDto;
    }
}
