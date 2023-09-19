package cn.ly.blog.controller;

import cn.ly.framework.annotation.SystemLog;
import cn.ly.framework.domain.vo.ArticleDetailVo;
import cn.ly.framework.domain.vo.HotArticleVo;
import cn.ly.framework.domain.vo.PageVo;
import cn.ly.framework.service.ArticleService;
import cn.ly.framework.utils.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 文章表(Article)表控制层
 *
 * @author Rêve
 * @since 2023-04-05 10:20:00
 */
@RestController
@RequestMapping("/article")
public class ArticleController {
    /**
     * 服务对象
     */
    @Resource
    private ArticleService articleService;

    /**
     * @param 
     * @return Result
     * @date 2023/4/6 10:08
     * @description 查询热门文章
     */
    @GetMapping("/hotArticleList")
    @SystemLog("查询热门文章")
    public Result hotArticleList(){
        List<HotArticleVo> hotArticleVos = articleService.hotArticleList();
        return Result.success(hotArticleVos);
    }

    /**
     * @param pageNum
     * @param pageSize
     * @param categoryId
     * @return Result
     * @date 2023/4/5 16:38
     * @description 根据分类ID，页面大小与页号去进行查询分页数据
     */
    @GetMapping("/articleList")
    @SystemLog("条件查询文章")
    public Result getAricleList(Integer pageNum,Integer pageSize,Long categoryId,String keyWords){
        PageVo articleList = articleService.getArticleList(pageNum, pageSize, categoryId,keyWords);
        return Result.success(articleList);
    }

    /**
     * @param id
     * @return Result
     * @date 2023/4/6 10:11
     * @description 根据id获取文章详情
     */
    @GetMapping("{id}")
    @SystemLog("文章详情")
    public Result getArticleDetail(@PathVariable("id") Long id){
        ArticleDetailVo articleDetailVo=articleService.getArticleDetail(id);
        return Result.success(articleDetailVo);
    }


    @PutMapping("/updateViewCount/{id}")
    public Result updateViewCount(@PathVariable Long id){
        return articleService.updateViewCount(id);
    }

}

