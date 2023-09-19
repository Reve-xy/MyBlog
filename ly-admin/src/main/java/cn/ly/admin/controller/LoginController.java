package cn.ly.admin.controller;

import cn.hutool.core.map.MapUtil;
import cn.ly.framework.annotation.CacheDel;
import cn.ly.framework.annotation.SystemLog;
import cn.ly.framework.constants.RedisKeyConstants;
import cn.ly.framework.domain.dto.LoginDto;
import cn.ly.framework.domain.dto.RegisterDto;
import cn.ly.framework.domain.entity.Article;
import cn.ly.framework.domain.vo.IndexDataVo;
import cn.ly.framework.domain.vo.UserBaseInfoVo;
import cn.ly.framework.domain.vo.UserInfoVo;
import cn.ly.framework.enums.HttpCodeEnum;
import cn.ly.framework.service.*;
import cn.ly.framework.utils.Result;
import cn.ly.framework.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.security.Key;
import java.util.List;

/**
 * 简要描述
 *
 * @author : Rêve
 * @version : 1.0
 * @date : 2023/4/22 20:11
 */
@Slf4j
@RestController
public class LoginController extends BaseController {
    @Resource
    private AdminLoginService adminLoginService;

    @Resource
    private RoleService roleService;

    @Resource
    private MenuService menuService;

    @Resource
    ArticleService articleService;

    @Resource
    CategoryService categoryService;
    @Resource
    TagService tagService;

    @Resource
    UserService userService;

    /**
     * @param
     * @return Result
     * @date 2023/4/23 21:45
     * @description 根据当前已登录的用户进行获取用户基本信息
     */
    @GetMapping("/getInfo")
    @SystemLog("获取用户基本信息")
    public Result getInfo(){
        Long userId = SecurityUtils.getUserId();
        UserBaseInfoVo userBaseInfoVo =adminLoginService.getUserInfoById(userId);
        List<String> rolesList= roleService.getRoleKeyByUserId(userId);
        List<String> permsList=menuService.getPermsByUserId(userId);
        UserInfoVo userInfoVo = new UserInfoVo(userBaseInfoVo, permsList, rolesList);
        return Result.success(userInfoVo);
    }

    @GetMapping("/getIndexData")
    @SystemLog("获取基本数据信息")
    public Result getIndexData(){
        List<Article> list = articleService.list();
        Integer viewCount = list.stream().mapToInt(a -> Math.toIntExact(a.getViewCount())).sum();
        return Result.success(new IndexDataVo(userService.count(),list.size(),categoryService.count(),tagService.count()    ,viewCount));
    }

    /**
     * @param loginDto
     * @return Result
     * @date 2023/4/23 15:58
     * @description 登录接口，返回token
     */
    @PostMapping("/login")
    @CacheDel(redisPrefix = RedisKeyConstants.USER)
    @SystemLog("登录")
    public Result login(@Valid @RequestBody LoginDto loginDto){
        String jwt = adminLoginService.login(loginDto);
        return Result.success(HttpCodeEnum.SUCCESS_LOGIN, MapUtil.builder().put("token",jwt).build());
    }

    /**
     * @param registerDto
     * @return Result
     * @date 2023/4/23 20:47
     * @description 注册接口，dto中性别是选填项
     */
    @PostMapping("/register")
    @SystemLog("注册")
    @CacheDel(redisPrefix = RedisKeyConstants.USER)
    public Result register(@Valid @RequestBody RegisterDto registerDto){
        adminLoginService.register(registerDto);
        return Result.success(HttpCodeEnum.SUCCESS_REGISTER);
    }


    /**
     * @param
     * @return Result
     * @date 2023/4/23 21:26
     * @description 退出当前系统
     */
    @PostMapping("/logout")
    @CacheDel(redisPrefix = RedisKeyConstants.USER)
    @SystemLog("退出系统")
    public Result logout(){
        redisCache.deleteObject(RedisKeyConstants.ADMIN_LOGIN_KEY+SecurityUtils.getUserId());
        return Result.success(HttpCodeEnum.SUCCESS_LOGOUT);
    }

}
