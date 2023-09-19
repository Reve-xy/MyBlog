package cn.ly.admin.controller;

import cn.ly.framework.annotation.CacheDel;
import cn.ly.framework.annotation.CacheFind;
import cn.ly.framework.annotation.SystemLog;
import cn.ly.framework.constants.RedisKeyConstants;
import cn.ly.framework.domain.entity.Link;
import cn.ly.framework.domain.vo.PageVo;
import cn.ly.framework.service.LinkService;
import cn.ly.framework.utils.Result;
import com.sun.org.glassfish.external.statistics.annotations.Reset;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 简要描述
 *
 * @author : Rêve
 * @version : 1.0
 * @date : 2023/6/8 23:38
 */
@RestController
@RequestMapping("/content/link")
public class LinkController {
    @Resource
    LinkService linkService;
    /**
     * 获取友链列表
     */
    @GetMapping("/list")
    @SystemLog("获取友链列表")
    @PreAuthorize("@ps.hasPermission('content:link:query')")
    public Result list(Link link, Integer pageNum, Integer pageSize)
    {
        PageVo pageVo = linkService.selectLinkPage(link,pageNum,pageSize);
        return Result.success(pageVo);
    }

    @PostMapping
    @SystemLog("新增友链")
    @PreAuthorize("@ps.hasPermission('content:link:add')")
    public Result add(@RequestBody Link link){
        linkService.save(link);
        return Result.success();
    }

    @DeleteMapping()
    @SystemLog("删除友链")
    @PreAuthorize("@ps.hasPermission('content:link:remove')")
    public Result delete(@RequestBody List<String> ids){
        linkService.removeByIds(ids);
        return Result.success();
    }

    @PutMapping
    @SystemLog("修改友链")
    @PreAuthorize("@ps.hasPermission('content:link:edit')")
    public Result edit(@RequestBody Link link){
        linkService.updateById(link);
        return Result.success();
    }

    @PutMapping("/changeLinkStatus")
    public Result changeLinkStatus(@RequestBody Link link){
        linkService.updateById(link);
        return Result.success();
    }



    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable(value = "id")Long id){
        Link link = linkService.getById(id);
        return Result.success(link);
    }
}
