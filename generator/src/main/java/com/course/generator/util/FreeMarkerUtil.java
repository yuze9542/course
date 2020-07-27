package com.course.generator.util;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class FreeMarkerUtil {

    static String ftlPath = "generator/src/main/java/com/course/generator/ftl/";   //模板路径
    static Template temp;

    public static void initConfig(String ftlName) throws IOException {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_29);    //选择包版本
        cfg.setDirectoryForTemplateLoading(new File(ftlPath));
        cfg.setObjectWrapper(new DefaultObjectWrapper(Configuration.VERSION_2_3_29));
        temp = cfg.getTemplate(ftlName);        //上面四个都是读模板

    }

    public static void generator(String fileName) throws IOException, TemplateException {
        FileWriter fw = new FileWriter(fileName);//生成什么文件
        BufferedWriter bw = new BufferedWriter(fw);//   //放到buffer
        temp.process(null, bw); //生成中...
        bw.flush();
        fw.close();
    }


}
