package cn.ly.blog.controller;


import cn.ly.framework.annotation.SystemLog;
import cn.ly.framework.constants.SystemConstants;
import cn.ly.framework.domain.entity.Comment;
import cn.ly.framework.domain.vo.PageVo;
import cn.ly.framework.enums.HttpCodeEnum;
import cn.ly.framework.service.CommentService;
import cn.ly.framework.utils.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 评论表(Comment)表控制层
 *
 * @author Rêve
 * @since 2023-04-09 21:58:01
 */
@RestController
@RequestMapping("/comment")
public class CommentController {
    /**
     * 服务对象
     */
    @Resource
    private CommentService commentService;

    /**
     * @param articleId
     * @param pageNum
     * @param pageSize
     * @return Result
     * @date 2023/4/9 22:06
     * @description 获取评论
     */
    @GetMapping("/commentList")
    @SystemLog("获取评论列表")
    public Result getCommentList(Long articleId,Integer pageNum,Integer pageSize){
        PageVo commentVos=commentService.getCommentList(SystemConstants.ARTICLE_COMMENT,articleId,pageNum,pageSize);
        return Result.success(commentVos);
    }

    @PostMapping
    @SystemLog("添加评论")
    public Result addComment(@RequestBody Comment comment){
        commentService.addComment(comment);
        return Result.success(HttpCodeEnum.SUCCESS);
    }

    @GetMapping("/linkCommentList")
    @SystemLog("友情链接评论列表")
    public Result linkCommentList(Integer pageNum,Integer pageSize){
        PageVo commentVos=commentService.getCommentList(SystemConstants.LINK_COMMENT,null,pageNum,pageSize);
        return Result.success(commentVos);
    }
}

