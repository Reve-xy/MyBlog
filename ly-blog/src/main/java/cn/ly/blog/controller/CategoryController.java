package cn.ly.blog.controller;

import cn.ly.framework.annotation.SystemLog;
import cn.ly.framework.domain.vo.CategoryVo;
import cn.ly.framework.service.CategoryService;
import cn.ly.framework.utils.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 分类表(Category)表控制层
 *
 * @author Rêve
 * @since 2023-04-05 14:47:13
 */
@RestController
@RequestMapping("/category")
public class CategoryController {
    /**
     * 服务对象
     */
    @Resource
    private CategoryService categoryService;

    /**
     * @param
     * @return Result
     * @date 2023/4/6 10:10
     * @description 获取分类列表
     */
    @GetMapping("/getCategoryList")
    @SystemLog("前台获取分类列表")
    public Result getCategoryList(){
        List<CategoryVo> categoryList = categoryService.getCategoryList();
        return Result.success(categoryList);
    }
}

