package cn.ly.admin.controller;


import cn.ly.framework.annotation.CacheDel;
import cn.ly.framework.annotation.SystemLog;
import cn.ly.framework.constants.RedisKeyConstants;
import cn.ly.framework.domain.dto.TagDto;
import cn.ly.framework.domain.dto.TagListDto;
import cn.ly.framework.domain.entity.Tag;
import cn.ly.framework.domain.vo.TagVo;
import cn.ly.framework.service.TagService;
import cn.ly.framework.utils.BeanCopyUtils;
import cn.ly.framework.utils.Result;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 标签(Tag)表控制层
 *
 * @author Rêve
 * @since 2023-04-14 11:17:57
 */
@RestController
@RequestMapping("/content/tag")
public class TagController {
    /**
     * 服务对象
     */
    @Resource
    private TagService tagService;

    /**
     * @param pageNum
     * @param pageSize
     * @param tagListDto
     * @return Result
     * @date 2023/4/15 22:01
     * @description 根据分页进行查询标签
     */
    @GetMapping("/list")
    @SystemLog("获取标签列表")
    @PreAuthorize("@ps.hasPermission('content:tag:list')")
    public Result listTag(Integer pageNum, Integer pageSize, TagListDto tagListDto) {
        return tagService.pageTagList(pageNum, pageSize, tagListDto);
    }

    @GetMapping("/listAllTag")
    @SystemLog("列出所有标签")
    public Result listAllTag(){
        return tagService.listAllTag();
    }

    @PostMapping
    @SystemLog("添加新标签")
    public Result addTag(@RequestBody TagListDto tagListDto){
        Tag tag = BeanCopyUtils.copyBean(tagListDto, Tag.class);
        tagService.save(tag);
        return Result.success();
    }

    @DeleteMapping
    @SystemLog("根据id删除标签")
    public Result delTag(@RequestBody List<String> ids){
        tagService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping("/{id}")
    @SystemLog("标签信息回显")
    public Result getTag(@PathVariable("id") Long id){
        Tag tag = tagService.getById(id);
        TagVo tagVo = BeanCopyUtils.copyBean(tag, TagVo.class);
        return Result.success(tag);
    }

    @PutMapping
    @SystemLog("更新标签信息")
    public Result updateTag(@RequestBody TagDto tagDto){
        Tag tag = BeanCopyUtils.copyBean(tagDto, Tag.class);
        tagService.updateById(tag);
        return Result.success();
    }
}

