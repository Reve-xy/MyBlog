package cn.ly.framework.service.impl;


import cn.ly.framework.annotation.CacheFind;
import cn.ly.framework.constants.RedisKeyConstants;
import cn.ly.framework.constants.SystemConstants;
import cn.ly.framework.domain.dto.*;
import cn.ly.framework.domain.entity.LoginUser;
import cn.ly.framework.domain.entity.User;
import cn.ly.framework.domain.entity.UserRole;
import cn.ly.framework.domain.vo.UserInfoVo;
import cn.ly.framework.domain.vo.UserVo;
import cn.ly.framework.enums.HttpCodeEnum;
import cn.ly.framework.exception.SystemException;
import cn.ly.framework.mapper.UserMapper;
import cn.ly.framework.service.EmailService;
import cn.ly.framework.service.RoleService;
import cn.ly.framework.service.UserRoleService;
import cn.ly.framework.service.UserService;
import cn.ly.framework.utils.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author 刘易
 * @since 2023-04-24
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    PasswordEncoder passwordEncoder;

    @Resource
    CheckRepeatUtils checkRepeatUtils;

    @Resource
    RoleService roleService;

    @Resource
    RedisCache redisCache;

    /**
     * @param queryUserDto
     * @return List<User>
     * @date 2023/4/27 16:38
     * @description 获取用户列表
     */
    @Override
    @CacheFind(key = RedisKeyConstants.USER_LIST_KEY)
    public List<User> getUserList(QueryUserDto queryUserDto) {
        String userName = queryUserDto.getUserName();
        String phoneNumber = queryUserDto.getPhoneNumber();
        String status = queryUserDto.getStatus();
        String  type = queryUserDto.getType();
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(StringUtils.hasText(userName),User::getUserName,userName);
        lambdaQueryWrapper.like(StringUtils.hasText(phoneNumber),User::getPhoneNumber,phoneNumber);
        lambdaQueryWrapper.eq(StringUtils.hasText(status),User::getStatus,status);
        lambdaQueryWrapper.eq(StringUtils.hasText(type),User::getType,type);
        lambdaQueryWrapper.orderByDesc(User::getType);
        PageHelper.startPage(queryUserDto.getPageNum(),queryUserDto.getPageSize());
        List<User> list = list(lambdaQueryWrapper);
        PageInfo<User> userPageInfo = new PageInfo<>(list);
        return userPageInfo.getList();
    }

    @Resource
    UserRoleService userRoleService;

    /**
     * @param addUserDto
     * @return void
     * @date 2023/4/27 16:35
     * @description 新增用户
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertUser(AddUserDto addUserDto) {
        User user = BeanCopyUtils.copyBean(addUserDto, User.class);
        checkRepeatUtils.setType(SystemConstants.UPDATE).checkUserNameRepeat(user).checkNickNameRepeat(user).checkEmailRepeat(user).checkPhoneNumberRepeat(user);
        String encode = passwordEncoder.encode(user.getPassword());
        user.setPassword(encode);
        //超管添加的才是管理员账号
        user.setType(SystemConstants.ADMIN_TYPE);
        save(user);
        if(user.getRoleIds()!=null&&user.getRoleIds().length>0){
            insertUserRole(user);
        }
    }

    /**
     * @param updateUserDto
     * @return void
     * @date 2023/4/27 17:34
     * @description 更新用户
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(UpdateUserDto updateUserDto) {
        User user = BeanCopyUtils.copyBean(updateUserDto, User.class);
        checkRepeatUtils.setType(SystemConstants.UPDATE).checkNickNameRepeat(user).checkEmailRepeat(user).checkPhoneNumberRepeat(user);
        // 删除用户与角色关联
        LambdaQueryWrapper<UserRole> userRoleUpdateWrapper = new LambdaQueryWrapper<>();
        userRoleUpdateWrapper.eq(UserRole::getUserId,user.getId());
        userRoleService.remove(userRoleUpdateWrapper);
        if(user.getRoleIds()!=null&&user.getRoleIds().length>0){
            insertUserRole(user);
        }
        updateById(user);
    }

    @Override
    public UserVo getUserInfo() {
        //获取当前用户id
        Long userId = SecurityUtils.getUserId();
        //根据用户id查询用户信息
        User user = getById(userId);
        //封装成UserInfoVo
        UserVo vo = BeanCopyUtils.copyBean(user,UserVo.class);
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updateUserInfo(User user) {
        updateById(user);
        return Result.success();
    }

    @Resource
    UserMapper userMapper;

    @Resource
    EmailService emailService;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result register(RegisterDto registerDto) {
        if (StringUtils.hasText(registerDto.getSex())) {
            // TODO: 2023/4/24 这里可以做优化，比如用自定义注解去校验sex，采取分组校验可实现字段存在在校验，不存在不校验
            if (!registerDto.getSex().equals("0") || !registerDto.getSex().equals("1")) {
                throw new SystemException(HttpCodeEnum.SEX_ERR);
            }
        }
        else{
            registerDto.setSex(SystemConstants.DEFAULT_SEX);
        }
        emailService.verifyEmailCaptcha(registerDto.getEmail(),registerDto.getCaptcha());
        if(StringUtils.hasText(registerDto.getPassword())&&!registerDto.getPassword().equals(registerDto.getConfirmPassword())){
            throw new SystemException(HttpCodeEnum.PWD_NOT_SAME);
        }
        User user = BeanCopyUtils.copyBean(registerDto, User.class);
        String encode = passwordEncoder.encode(user.getPassword());
        user.setPassword(encode);
        user.setType(SystemConstants.NORMAL_TYPE);
        user.setStatus(SystemConstants.STATUS_NORMAL);

        userMapper.insert(user);
        userRoleService.save(new UserRole(user.getId(), SystemConstants.NORMAL_ROLE));
        return Result.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateLoginUser(UpdateLoginUserDto updateLoginUserDto) {
        User user = BeanCopyUtils.copyBean(updateLoginUserDto, User.class);
        user.setId(SecurityUtils.getUserId());
        checkRepeatUtils.setType(SystemConstants.UPDATE).checkEmailRepeat(user).checkPhoneNumberRepeat(user).checkNickNameRepeat(user);
        updateById(user);
        //更新缓存的数据
        User loginUser = SecurityUtils.getUser();
        loginUser.setNickName(user.getNickName()).setSex(user.getSex()).
                setEmail(user.getEmail()).setPhoneNumber(user.getPhoneNumber());
        redisCache.resetExpire(RedisKeyConstants.ADMIN_LOGIN_KEY,new LoginUser(loginUser,SecurityUtils.getPermissions()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result resetPwd(ResetPwdDto resetPwdDto) {
        String cacheEmail = (String)redisCache.getCacheObject(RedisKeyConstants.RESET_EMAIL_KEY + resetPwdDto.getEmail());
        if(!StringUtils.hasText(cacheEmail)){
            throw new SystemException(HttpCodeEnum.CERTIFICATE_EXPIRE);
        }else{
            String password = resetPwdDto.getPassword();
            String confirmPassword = resetPwdDto.getConfirmPassword();
            if(!password.equals(confirmPassword)){
                throw new SystemException(HttpCodeEnum.PWD_NOT_SAME);
            }else{
                update(new User().setPassword(passwordEncoder.encode(password)), Wrappers.<User>lambdaQuery().eq(User::getEmail,resetPwdDto.getEmail()));
                //认证后删除
                redisCache.deleteObject(RedisKeyConstants.RESET_EMAIL_KEY + cacheEmail);
                return Result.success();
            }
        }
    }

    /**
     * @param user
     * @return void
     * @date 2023/4/27 16:40
     * @description 新增用户与角色对应关系
     */
    private void insertUserRole(User user){

        List<UserRole> userRoles = Arrays.stream(user.getRoleIds()).map(roleId -> new UserRole(user.getId(), roleId)).collect(Collectors.toList());

        userRoleService.saveBatch(userRoles);
    }
}
