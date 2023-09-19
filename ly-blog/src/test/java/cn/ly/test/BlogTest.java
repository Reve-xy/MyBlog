package cn.ly.test;


import cn.ly.LyBlogApplication;
import cn.ly.framework.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * 简要描述
 *
 * @author : Rêve
 * @version : 1.0
 * @date : 2023/4/5 15:54
 */
@SpringBootTest(classes = LyBlogApplication.class)
public class BlogTest {
    @Resource
    CategoryService categoryService;

}
