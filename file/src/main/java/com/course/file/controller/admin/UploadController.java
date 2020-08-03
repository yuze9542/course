package com.course.file.controller.admin;

import com.course.server.dto.FileDto;
import com.course.server.dto.ResponseDto;
import com.course.server.enums.FileUseEnum;
import com.course.server.service.FileService;
import com.course.server.service.TestService;
import com.course.server.util.Base64ToMultipartFile;
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

    //file 必须要和前端名字一致 formData的key一致
    @PostMapping("/upload")
    public ResponseDto upload(@RequestBody FileDto fileDto) throws IOException {
        log.info("上传文件开始:");
        String use = fileDto.getUse();
        String key = fileDto.getKey();
        String suffix = fileDto.getSuffix();
        String shardBase64 = fileDto.getShard();
        MultipartFile shard = Base64ToMultipartFile.base64ToMultipart(shardBase64);
        //保存文件到本地
        FileUseEnum useEnum = FileUseEnum.getByCode(use);
        //如果不存在则创建
        String dir = useEnum.name().toLowerCase();
        File fullDir = new File(FILE_PATH+dir);
        if(!fullDir.exists()){
            fullDir.mkdir();
        }

        //File.separator是产生斜杠的意思
//        String path = dir + File.separator + key + "." + suffix+"."+fileDto.getShardIndex();
        String path = new StringBuffer(dir)
                .append(File.separator)
                .append(key)
                .append(".")
                .append(suffix)
                .toString(); // course\6sfSqfOwzmik4A4icMYuUe.mp4
        String localPath = new StringBuffer(path)
                .append(".")
                .append(fileDto.getShardIndex())
                .toString(); // course\6sfSqfOwzmik4A4icMYuUe.mp4.1
        String fullpath = FILE_PATH +localPath;
        File dest = new File(fullpath);//找到文件夹 需要提前创建好
        shard.transferTo(dest);//将文件写到全路径
        log.info(dest.getAbsolutePath());
        log.info("保存文件记录开始");
        fileDto.setPath(path);
        fileService.save(fileDto);

        ResponseDto responseDto = new ResponseDto();
        fileDto.setPath(FILE_DOMAIN+path);
        responseDto.setContent(fileDto);

        if (fileDto.getShardIndex() == fileDto.getShardTotal()){
            this.merge(fileDto);
        }
        return responseDto;
    }

    public void merge(FileDto fileDto) throws IOException {
        log.info("开始合并分片:");
        String path = fileDto.getPath();
        path = path.replace(FILE_DOMAIN, ""); //course\6sfSqfOwzmik4A4icMYuUe.mp4
        Integer shardTotal = fileDto.getShardTotal();
        File newFile = new File(FILE_PATH + path);   //最终输出的文件
        FileOutputStream outputStream = new FileOutputStream(newFile, true); //文件追加写入
        FileInputStream fileInputStream = null; //分片文件
        byte[] byt = new byte[1 * 1024 * 1024];
        int len;
        try {
            for (int i = 0; i < shardTotal; i++) {
                fileInputStream = new FileInputStream(new File(FILE_PATH + path+"."+(i+1)));
                while ((len = fileInputStream.read(byt)) != -1) {
                    outputStream.write(byt, 0, len);
                }
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
        log.info("合并分片结束");
    }
}
