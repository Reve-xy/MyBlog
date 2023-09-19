package cn.ly.test;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.TemplateType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

/**
 * 简要描述
 *
 * @author : Rêve
 * @version : 1.0
 * @date : 2023/3/29 15:56
 */
public class FastAutoGeneratorTest {
    public static void main(String[] args) {
        String url = "jdbc:mysql://127.0.0.1:3306/sg_blog?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai";
        FastAutoGenerator.create(url, "root", "ly2lym1314")
                .globalConfig(builder -> {
                    builder.author("刘易") // 设置作者
                            //.enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir("E:\\Java\\Project_Code\\springboot_project\\blog_project\\LyBlog\\ly-framework\\src\\main\\java"); // 指定输出目录
                    //
                })
                .templateConfig(builder ->{
                    //Controller通过EasyCode进行生成
                    builder.disable(TemplateType.CONTROLLER);
                })
                .packageConfig(builder -> {
                    builder.parent("cn") // 设置父包名
                            //app manager
                            .moduleName("ly") // 设置父包模块名
                            .entity("domain.entity")
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml
                                    , "E:\\Java\\Project_Code\\springboot_project\\blog_project\\LyBlog\\ly-framework\\src\\main\\resources\\mapper")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {// 设置需要生成的表名
                    builder.addInclude("sg_article_tag")
                            .addTablePrefix("sg")
                            .entityBuilder().enableLombok().enableTableFieldAnnotation()
                            .controllerBuilder().enableRestStyle()
                            .serviceBuilder().formatServiceFileName("%sService")
                            .mapperBuilder().enableBaseResultMap()
                          /*  .addInclude("sys_menu")
                            .addInclude("sys_role_menu","sys_user_role")*/
                            ; // 设置过滤表前缀
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }

}
