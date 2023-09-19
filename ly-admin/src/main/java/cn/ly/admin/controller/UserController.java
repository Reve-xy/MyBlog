package cn.ly.admin.controller;



import cn.ly.framework.annotation.CacheDel;
import cn.ly.framework.annotation.SystemLog;
import cn.ly.framework.constants.RedisKeyConstants;
import cn.ly.framework.constants.SystemConstants;
import cn.ly.framework.domain.dto.*;
import cn.ly.framework.domain.entity.LoginUser;
import cn.ly.framework.domain.entity.Role;
import cn.ly.framework.domain.entity.User;
import cn.ly.framework.domain.vo.PageVo;
import cn.ly.framework.domain.vo.UserInfoAndRoleIdsVo;
import cn.ly.framework.domain.vo.UserProfileVo;
import cn.ly.framework.domain.vo.UserVo;
import cn.ly.framework.enums.HttpCodeEnum;
import cn.ly.framework.exception.SystemException;
import cn.ly.framework.service.MenuService;
import cn.ly.framework.service.RoleService;
import cn.ly.framework.service.UploadService;
import cn.ly.framework.service.UserService;

import cn.ly.framework.utils.BeanCopyUtils;
import cn.ly.framework.utils.Result;
import cn.ly.framework.utils.SecurityUtils;
import org.apache.ibatis.annotations.Update;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.xml.crypto.Data;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户表(User)表控制层
 *
 * @author Rêve
 * @since 2023-04-22 16:47:47
 */
@RestController
@RequestMapping("/system/user")
public class UserController extends BaseController {
    /**
     * 服务对象
     */
    @Resource
    private UserService userService;

    @Resource
    private RoleService roleService;

    @Resource
    private MenuService menuService;

    /**
     * @param queryUserDto
     * @return Result
     * @date 2023/4/27 15:03
     * @description 用户列表
     */
    @GetMapping("/list")
    @PreAuthorize("@ps.hasPermission('system:user:list')")
    @SystemLog("用户列表")
    public Result listUser(@Valid QueryUserDto queryUserDto) {
        List<User> users=userService.getUserList(queryUserDto);
        List<UserVo> userVos = BeanCopyUtils.copyBeanList(users, UserVo.class);
        List<UserVo> userVoList = userVos.stream().map(m ->
                m.setRoleGroups(roleService.getRoleNameById(roleService.getRoleIdByUserId(m.getId()))))
                .collect(Collectors.toList());
        return Result.success(new PageVo<UserVo>(userVoList, (long) userVoList.size()));
    }

    @PutMapping("/changeStatus")
    @PreAuthorize("@ps.hasPermission('system:user:edit')")
    @CacheDel(redisPrefix = RedisKeyConstants.USER_LIST_KEY)
    @SystemLog("更改用户状态")
    public Result changeStatus(@RequestBody  User user){
        if(user.getId().equals(SystemConstants.ADMIN_ID)){
            throw new SystemException(HttpCodeEnum.NO_OPERATOR_AUTH);
        }
        userService.updateById(user);
        return Result.success();
    }

    /**
     * @param id
     * @return Result
     * @date 2023/5/4 17:06
     * @description 修改用户时用户与关联的角色信息回显
     */
    @GetMapping("/{id}")
    @SystemLog("用户信息回显")
    public Result getUserRoleById(@NotNull(message = "请选择用户") @PathVariable Long id){
        List<Role> roles = roleService.getAllRole();
        User user = userService.getById(id);
        //当前用户所具有的角色id列表
        List<Long> roleIds = roleService.getRoleIdByUserId(id);
        UserVo userVo = BeanCopyUtils.copyBean(user, UserVo.class);
        UserInfoAndRoleIdsVo vo = new UserInfoAndRoleIdsVo(userVo,roles,roleIds);
        return Result.success(vo);
    }

    //对已登录的用户操作
    @GetMapping("/profile")
    @SystemLog("用户个人中心信息回显")
    public Result getProfile(){
        User user = userService.getById(SecurityUtils.getUserId());
        List<Long> roleIdByUserId = roleService.getRoleIdByUserId(user.getId());
        List<String> roleGroups= roleService.getRoleNameById(roleIdByUserId);
        UserProfileVo userProfileVo = BeanCopyUtils.copyBean(user, UserProfileVo.class);
        userProfileVo.setRoleGroups(roleGroups);
        return Result.success(userProfileVo);
    }

    @PutMapping("/updateLoginUser")
    @CacheDel(redisPrefix = RedisKeyConstants.USER_INFO_KEY)
    @SystemLog("修改当前登录用户的信息")
    public Result updateLoginUser(@Valid @RequestBody UpdateLoginUserDto updateLoginUserDto){
        userService.updateLoginUser(updateLoginUserDto);
        return Result.success();
    }

    @Resource
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @PutMapping("/updateUserPwd")
    @CacheDel(redisPrefix = RedisKeyConstants.USER_INFO_KEY)
    @PreAuthorize("@ps.hasPermission('system:user:resetPwd')")
    @SystemLog("修改密码")
    public Result updateUserPwd( @RequestBody UpdateUserPwd updateUserPwd){
        User user = SecurityUtils.getLoginUser().getUser();
        if(bCryptPasswordEncoder.matches(updateUserPwd.getOldPassword(),user.getPassword())){
            user.setPassword(bCryptPasswordEncoder.encode(updateUserPwd.getNewPassword()));
            userService.updateById(user);
            return Result.success();
        }else{
            return Result.failure(400,"旧密码错误");
        }
    }

    @Resource(name="localUploadService")
    UploadService uploadService;

    @PostMapping("/uploadAvatar")
    @SystemLog("上传头像")
    @CacheDel(redisPrefix = RedisKeyConstants.USER_INFO_KEY)
    public Result uploadImg(@RequestParam("avatar")  MultipartFile avatar){
        return uploadService.uploadAvatar(avatar);
    }


    ///////////////////////////////////////////////////////////////////////////
    // user的crud开始
    ///////////////////////////////////////////////////////////////////////////
    /**
     * @param addUserDto
     * @return Result
     * @date 2023/4/27 17:07
     * @description 新增用户
     */
    @PostMapping
    @PreAuthorize("@ps.hasPermission('system:user:add')")
    @SystemLog("新增用户")
    @CacheDel(redisPrefix = RedisKeyConstants.USER)
    public Result addUser(@RequestBody AddUserDto addUserDto){
        userService.insertUser(addUserDto);
        return Result.success();
    }

    /**
     * @param ids
     * @return Result
     * @date 2023/4/27 17:10
     * @description 删除用户
     */
    @DeleteMapping
    @PreAuthorize("@ps.hasPermission('system:user:del')")
    @SystemLog("删除用户")
    @CacheDel(redisPrefix = RedisKeyConstants.USER)
    public Result delUser(@RequestBody List<String> ids){
        userService.removeByIds(ids);
        return Result.success();
    }

    /**
     * @param updateUserDto
     * @return Result
     * @date 2023/5/4 16:51
     * @description 更新用户
     */
    @PutMapping
    @PreAuthorize("@ps.hasPermission('system:user:edit')")
    @SystemLog("更新用户")
    @CacheDel(redisPrefix = RedisKeyConstants.USER)
    public Result updateUser(@Valid @RequestBody UpdateUserDto updateUserDto){
        userService.updateUser(updateUserDto);
        return Result.success();
    }






}

