package cn.ly.blog.controller;


import cn.ly.framework.annotation.CacheDel;
import cn.ly.framework.annotation.SystemLog;
import cn.ly.framework.constants.RedisKeyConstants;
import cn.ly.framework.domain.dto.RegisterDto;
import cn.ly.framework.domain.dto.ResetPwdDto;
import cn.ly.framework.domain.entity.User;
import cn.ly.framework.domain.vo.UserVo;
import cn.ly.framework.service.UserService;
import cn.ly.framework.utils.CheckRepeatUtils;
import cn.ly.framework.utils.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 用户表(User)表控制层
 *
 * @author Rêve
 * @since 2023-04-11 09:44:42
 */
@RestController
@RequestMapping("/user")
public class UserController {
    /**
     * 服务对象
     */
    @Resource
    private UserService userService;

    @GetMapping("/userInfo")
    @SystemLog("获取用户信息")
    public Result getUserInfo(){
        UserVo userVo=userService.getUserInfo();
        return Result.success(userVo);
    }

    @PutMapping("/userInfo")
    @SystemLog("更新用户信息")
    @CacheDel(redisPrefix = RedisKeyConstants.USER)
    public Result updateUserInfo(@RequestBody User user){
        return userService.updateUserInfo(user);
    }

    @PostMapping("/register")
    @SystemLog("注册")
    @CacheDel(redisPrefix = RedisKeyConstants.USER)
    public Result registerUser(@Valid @RequestBody RegisterDto registerDto){
        return userService.register(registerDto);
    }
    @PostMapping("/resetPwd")
    @SystemLog("重置密码")
    @CacheDel(redisPrefix = RedisKeyConstants.USER)
    public Result resetPwd(@Valid @RequestBody ResetPwdDto resetPwdDto){
        return userService.resetPwd(resetPwdDto);
    }

    @Resource
    CheckRepeatUtils checkRepeatUtils;

    @PostMapping("/checkEmail")
    @SystemLog("检查邮箱是否已经注册")
    public Result checkEmail(@RequestParam("email") String email){
        checkRepeatUtils.checkEmailRepeat(new User().setEmail(email));
        return Result.success();
    }

    @PostMapping("/checkUserName")
    @SystemLog("检查用户名是否已经注册")
    public Result checkUserName(@RequestParam("userName") String userName){
        checkRepeatUtils.checkUserNameRepeat(new User().setUserName(userName));
        return Result.success();
    }
}

