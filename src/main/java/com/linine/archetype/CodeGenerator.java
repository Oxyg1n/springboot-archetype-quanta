package com.linine.archetype;


import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CodeGenerator {
    public static void main(String[] args) {
        /* ************ 配置部分 ************ */
        String url = "jdbc:mysql://127.0.0.1:3306/test?serverTimezone=GMT%2B8&useUnicode=true&characterEncoding=utf-8&useSSL=false"; // 数据库url
        String username = "root"; // 数据库用户名
        String password = "18991899"; // 数据库密码
        List<String> tables = new ArrayList<>();
        // 添加要生成的表名
        tables.add("user");
        String parent = "com.linine"; // 项目包名 com.quanta
        String mode = "archetype"; // 模块名 archetype
        String author = "lin";
        /* ************ 配置部分 ************ */

        String projectPath = System.getProperty("user.dir");
        String outPutDir = projectPath + "/src/main/java";
        FastAutoGenerator.create(url, username, password)
                .globalConfig(builder -> {
                    builder.author(author) // 设置作者
                            .fileOverride() // 覆盖已生成文件
                            .outputDir(outPutDir); // 指定输出目录
                })
                // 包配置
                .packageConfig(builder -> {
                    // 父包名
                    builder.parent(parent)
                            // 模块名
                            .moduleName(mode)
                            .entity("entity")
                            .service("service")
                            .serviceImpl("service.impl")
                            .controller("controller")
                            .mapper("mapper")
                            .xml("mapper")
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, projectPath + "\\src\\main\\resources\\mapper"));
                })
                // 策略设置
                .strategyConfig(builder -> {
                    builder.addInclude(tables)
                            .serviceBuilder()
                            .formatServiceFileName("%sService")
                            .formatServiceImplFileName("%sServiceImpl")
                            .entityBuilder()
                            .enableLombok()
                            .logicDeleteColumnName("deleted")
                            .enableTableFieldAnnotation()
                            .controllerBuilder()
                            .formatFileName("%sController")
                            .enableRestStyle()
                            .mapperBuilder()
                            .enableBaseResultMap()  //生成通用的resultMap
                            .formatMapperFileName("%sMapper")
                            .enableMapperAnnotation()
                            .formatXmlFileName("%sMapper");
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}