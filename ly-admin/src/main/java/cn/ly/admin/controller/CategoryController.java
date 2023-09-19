package cn.ly.admin.controller;

import cn.ly.framework.annotation.CacheDel;
import cn.ly.framework.annotation.SystemLog;
import cn.ly.framework.constants.RedisKeyConstants;
import cn.ly.framework.domain.entity.Article;
import cn.ly.framework.domain.entity.Category;
import cn.ly.framework.domain.vo.CategoryVo;
import cn.ly.framework.domain.vo.ExcelCategoryVo;
import cn.ly.framework.enums.HttpCodeEnum;
import cn.ly.framework.service.CategoryService;
import cn.ly.framework.utils.BeanCopyUtils;
import cn.ly.framework.utils.Result;
import cn.ly.framework.utils.WebUtils;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 分类表(Category)表控制层
 *
 * @author Rêve
 * @since 2023-04-05 14:47:13
 */
@RestController
@RequestMapping("/content/category")
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
     * @description 分类列表
     */
    @GetMapping("/listAllCategory")
    @SystemLog("获取分类列表")
    public Result getCategoryList(){
        List<CategoryVo> categoryList = categoryService.getCategoryList();
        return Result.success(categoryList);
    }

    /**
     * @param response
     * @return void
     * @date 2023/6/8 15:30
     * @description 导出分类表
     */
    @GetMapping("/export")
    @SystemLog("导出分类表")
    @PreAuthorize("@ps.hasPermission('content:category:export')")
    public void export(HttpServletResponse response){
        try {
            //设置下载文件的请求头
            WebUtils.setDownLoadHeader("分类.xlsx",response);
            //获取需要导出的数据
            List<Category> categoryVos = categoryService.list();

            List<ExcelCategoryVo> excelCategoryVos = BeanCopyUtils.copyBeanList(categoryVos, ExcelCategoryVo.class);
            //把数据写入到Excel中
            EasyExcel.write(response.getOutputStream(), ExcelCategoryVo.class).autoCloseStream(Boolean.FALSE).sheet("分类导出")
                    .doWrite(excelCategoryVos);

        } catch (Exception e) {
            //如果出现异常也要响应json
            Result result = Result.failure(HttpCodeEnum.SYSTEM_ERROR);
            WebUtils.renderString(response, JSON.toJSONString(result));
        }
    }

    /**
     * @param category
     * @param pageNum
     * @param pageSize
     * @return Result
     * @date 2023/6/8 23:34
     * @description 后台分页获取分类列表
     */
    @GetMapping("/list")
    @SystemLog("后台分页获取分类列表")
    public Result getCategoryPage(Category category, Integer pageNum, Integer pageSize){
        return categoryService.selectCategoryPage(category.getName(),category.getStatus(),pageNum,pageSize);
    }

    @PutMapping
    @SystemLog("修改分类信息")
    public Result edit(@RequestBody Category category){
        categoryService.updateById(category);
        return Result.success();
    }

    @DeleteMapping(value = "/{id}")
    @SystemLog("删除分类")
    public Result remove(@PathVariable(value = "id")Long id){
        categoryService.removeById(id);
        return Result.success();
    }

    /**
     * @param id
     * @return Result
     * @date 2023/6/8 23:35
     * @description 修改时信息的回显
     */
    @GetMapping(value = "/{id}")
    @SystemLog("修改分类信息的回显")
    public Result getInfo(@PathVariable(value = "id")Long id){
        Category category = categoryService.getById(id);
        return Result.success(category);
    }

    @PutMapping("/changeStatus")
    @SystemLog("更改分类状态")
    public Result changeStatus(@RequestBody Category category){
        categoryService.updateById(category);
        return Result.success();
    }

    @PostMapping
    @SystemLog("创建分类")
    public Result add(@RequestBody Category category){
        categoryService.save(category);
        return Result.success();
    }

}


