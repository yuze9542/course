package com.course.generator.test;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TestUtil {

    static String ftlPath = "generator/src/main/java/com/course/generator/test/";   //模板路径
    static String toPath = "generator/src/main/java/com/course/generator/test/";    //生成路径

    public static void main(String[] args) throws IOException, TemplateException {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_29);    //选择包版本
        cfg.setDirectoryForTemplateLoading(new File(ftlPath));
        cfg.setObjectWrapper(new DefaultObjectWrapper(Configuration.VERSION_2_3_29));
        Template temp = cfg.getTemplate("test.ftl");        //上面四个都是读模板

        FileWriter fw = new FileWriter(toPath + "Test.java");//生成什么文件
        BufferedWriter bw = new BufferedWriter(fw);//   //放到buffer
        temp.process(null, bw); //生成中...
        bw.flush();
        fw.close();
    }
}