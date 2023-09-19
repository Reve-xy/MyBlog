package cn.ly.admin.controller;

import cn.ly.framework.annotation.CacheDel;
import cn.ly.framework.annotation.SystemLog;
import cn.ly.framework.constants.RedisKeyConstants;
import cn.ly.framework.domain.dto.AddArticleDto;
import cn.ly.framework.domain.dto.ArticleDto;
import cn.ly.framework.domain.entity.Article;
import cn.ly.framework.domain.entity.User;
import cn.ly.framework.domain.vo.PageVo;
import cn.ly.framework.service.ArticleService;
import cn.ly.framework.utils.Result;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 简要描述
 *
 * @author : Rêve
 * @version : 1.0
 * @date : 2023/4/16 9:51
 */
@RestController
@RequestMapping("/content/article")
public class ArticleController {
    @Resource
    ArticleService articleService;

    @GetMapping("/list")
    @SystemLog("文章列表")
    @PreAuthorize("@ps.hasPermission('content:article:list')")
    public Result list(Article article, Integer pageNum, Integer pageSize)
    {
        PageVo pageVo = articleService.selectArticlePage(article,pageNum,pageSize);
        return Result.success(pageVo);
    }

    @PostMapping
    @SystemLog("新增文章")
    @PreAuthorize("@ps.hasPermission('content:article:write')")
    @CacheDel(redisPrefix = "article:info")
    public Result addArticle(@RequestBody AddArticleDto addArticleDto) {
        return articleService.addArticle(addArticleDto);
    }

    @PutMapping("/changeStatus")
    @SystemLog("更改文章状态")
    @CacheDel(redisPrefix = "article:info")
    public Result changeStatus(@RequestBody Article article){
        articleService.updateById(article);
        return Result.success();
    }
    /**
     * @param id
     * @return Result
     * @date 2023/4/16 11:20
     * @description 修改文章的回显
     */
    @GetMapping("/{id}")
    @SystemLog("文章信息回显")
    public Result getArticleInfo(@PathVariable("id") Long id) {
        return articleService.getArticleInfo(id);
    }

    @PutMapping
    @SystemLog("更新文章信息")
    @PreAuthorize("@ps.hasPermission('content:article:write')")
    @CacheDel(redisPrefix = "article:info")
    public Result updateArticle(@RequestBody  ArticleDto articleDto) {
        return articleService.editArticle(articleDto);
    }

    @DeleteMapping("/{id}")
    @SystemLog("删除文章")
    @CacheDel(redisPrefix = "article:info")
    public Result delArticle(@PathVariable("id") Long id) {
        articleService.removeById(id);
        return Result.success();
    }
}
