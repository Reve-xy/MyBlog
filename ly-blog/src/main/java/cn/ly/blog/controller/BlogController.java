package cn.ly.blog.controller;

import cn.ly.framework.annotation.SystemLog;
import cn.ly.framework.domain.dto.LoginDto;
import cn.ly.framework.domain.vo.BlogUserLoginVo;
import cn.ly.framework.enums.HttpCodeEnum;
import cn.ly.framework.service.BlogLoginService;
import cn.ly.framework.utils.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 简要描述
 *
 * @author : Rêve
 * @version : 1.0
 * @date : 2023/4/6 14:03
 */
@RestController
public class BlogController {
    @Resource
    BlogLoginService blogLoginService;

    @PostMapping("/login")
    @SystemLog("博客前台登录")
    public Result login(@RequestBody LoginDto loginDto){
        BlogUserLoginVo blogUserLoginVo=blogLoginService.login(loginDto);
        return Result.success(blogUserLoginVo);
    }

    @PostMapping("/logout")
    @SystemLog("博客前台logout")
    public Result logout(){
       blogLoginService.logout();
        return Result.success(HttpCodeEnum.SUCCESS_LOGOUT);
    }
}