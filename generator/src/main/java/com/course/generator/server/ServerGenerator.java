package com.course.generator.server;
import com.course.generator.util.DbUtil;
import com.course.generator.util.Field;
import com.course.generator.util.FreemarkerUtil;

import java.util.*;

public class ServerGenerator {

    static String MODULE = "business";
    static String toDtoPath = "server/src/main/java/com/course/server/dto/";
    static String toServicePath = "server/src/main/java/com/course/server/service/";
    static String toControllerPath = "business/src/main/java/com/course/"+MODULE+"/controller/admin/";

    public static void main(String[] args) throws Exception {

        String Domain = "Section";
        String domain = "section";
        String tableNameCn = "小节";
        String module = MODULE;
        List<Field> fieldList = DbUtil.getColumnByTableName(domain);//  获取所有列信息
        Set<String> typeSet = getJavaTypes(fieldList);  //根据列信息获取集合
        Map<String,Object> map = new HashMap<>();
        map.put("Domain",Domain);
        map.put("domain",domain);
        map.put("tableNameCn",tableNameCn);
        map.put("module",module);
        map.put("fieldList",fieldList);
        map.put("typeSet",typeSet);

        //生成Dto
        FreemarkerUtil.initConfig("dto.ftl");
        FreemarkerUtil.generator(toDtoPath+Domain+"Dto.java",map);

        //生成Service
        FreemarkerUtil.initConfig("service.ftl");
        FreemarkerUtil.generator(toServicePath+Domain+"Service.java",map);

        //生成Controller
        FreemarkerUtil.initConfig("controller.ftl");
        FreemarkerUtil.generator(toControllerPath+Domain+"Controller.java",map);
    }

    private static Set<String> getJavaTypes(List<Field> fieldList) {
        Set<String> set = new HashSet<>();
        for (int i = 0; i < fieldList.size(); i++) {
            Field field = fieldList.get(i);
            set.add(field.getJavaType());
        }
        return set;
    }
}
