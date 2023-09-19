package cn.ly.blog.controller;


import cn.ly.framework.annotation.SystemLog;
import cn.ly.framework.domain.vo.LinkVo;
import cn.ly.framework.service.LinkService;
import cn.ly.framework.utils.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 友链(Link)表控制层
 *
 * @author Rêve
 * @since 2023-04-06 13:30:03
 */
@RestController
@RequestMapping("/link")
public class LinkController {
    /**
     * 服务对象
     */
    @Resource
    private LinkService linkService;

    /**
     * @param
     * @return Result
     * @date 2023/4/6 13:32
     * @description 获取所有审核通过的友链
     */
    @GetMapping("/getAllLink")
    @SystemLog("获取所有审核通过的友链")
    public Result getAllLink(){
        List<LinkVo> linkVoList=linkService.getAllLink();
        return Result.success(linkVoList);
    }


}

