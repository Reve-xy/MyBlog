package cn.ly.framework.service.impl;


import cn.ly.framework.annotation.CacheFind;
import cn.ly.framework.config.ReveConfig;
import cn.ly.framework.constants.RedisKeyConstants;
import cn.ly.framework.constants.SystemConstants;
import cn.ly.framework.domain.dto.LoginDto;
import cn.ly.framework.domain.dto.RegisterDto;
import cn.ly.framework.domain.entity.LoginUser;
import cn.ly.framework.domain.entity.User;
import cn.ly.framework.domain.vo.UserBaseInfoVo;
import cn.ly.framework.enums.HttpCodeEnum;
import cn.ly.framework.exception.SystemException;
import cn.ly.framework.mapper.UserMapper;
import cn.ly.framework.service.AdminLoginService;
import cn.ly.framework.service.CaptchaService;
import cn.ly.framework.utils.BeanCopyUtils;
import cn.ly.framework.utils.CheckRepeatUtils;
import cn.ly.framework.utils.JwtUtils;
import cn.ly.framework.utils.RedisCache;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 简要描述
 *
 * @author : Rêve
 * @version : 1.0
 * @date : 2023/5/2 22:28
 */
@Service
public class AdminLoginServiceImpl implements AdminLoginService {

    @Resource
    ReveConfig reveConfig;
    @Resource
    AuthenticationManager authenticationManager;
    @Resource
    JwtUtils jwtUtils;

    @Resource
    RedisCache redisCache;

    @Resource
    PasswordEncoder passwordEncoder;

    @Resource
    UserMapper userMapper;

    @Resource
    CheckRepeatUtils checkRepeatUtils;

    @Resource
    CaptchaService captchaService;
    /**
     * @param loginDto
     * @return String
     * @date 2023/4/24 19:32
     * @description 登录
     */
    @Override
    public String login(LoginDto loginDto) {
        String userName = loginDto.getUserName();
        if(!StringUtils.hasText(userName)){
            throw new SystemException(HttpCodeEnum.REQUIRE_ACCOUNT);
        }
        if(reveConfig.getCaptchaEnabled()){
            captchaService.verifyCaptcha(loginDto.getUuid(),loginDto.getCaptcha());
        }
        // 传入用户需要认证的信息
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userName, loginDto.getPassword());
        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        //权限为0，不是管理员
        if(Objects.isNull(loginUser.getPermissions())&&!loginUser.getUser().getId().equals(SystemConstants.ADMIN_ID)){
            throw new SystemException(HttpCodeEnum.NOT_ADMIN);
        }
        Long id = loginUser.getUser().getId();
        String jwt = jwtUtils.createJWT(id.toString());
        //登录后的用户存入redis
        redisCache.setCacheObject(RedisKeyConstants.ADMIN_LOGIN_KEY + id, loginUser, jwtUtils.getExpire(), TimeUnit.HOURS);
        return jwt;
    }

    /**
     * @param registerDto
     * @return void
     * @date 2023/4/24 19:38
     * @description 注册
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(RegisterDto registerDto) {
        if (StringUtils.hasText(registerDto.getSex())) {
            // TODO: 2023/4/24 这里可以做优化，比如用自定义注解去校验sex，采取分组校验可实现字段存在在校验，不存在不校验
            if (!registerDto.getSex().equals("0") || !registerDto.getSex().equals("1")) {
                throw new SystemException(HttpCodeEnum.SEX_ERR);
            }
        }
        else{
            registerDto.setSex(SystemConstants.DEFAULT_SEX);
        }
        if(StringUtils.hasText(registerDto.getPassword())&&registerDto.getPassword().equals(registerDto.getConfirmPassword())){
            throw new SystemException(HttpCodeEnum.PWD_NOT_SAME);
        }
        User user = BeanCopyUtils.copyBean(registerDto, User.class);
        checkRepeatUtils.setType(SystemConstants.INSERT).checkUserNameRepeat(user).checkNickNameRepeat(user).checkEmailRepeat(user).checkPhoneNumberRepeat(user);
        String encode = passwordEncoder.encode(user.getPassword());
        user.setPassword(encode);
user.setType(SystemConstants.ADMIN_TYPE);
        userMapper.insert(user);


    }

    /**
     * @param userId
     * @return UserVo
     * @date 2023/4/24 14:23
     * @description 根据当前用户ID获取用户基本信息
     */
    @Override
    @CacheFind(key = RedisKeyConstants.USER_INFO_KEY)
    public UserBaseInfoVo getUserInfoById(Long userId) {
        User user = userMapper.selectById(userId);
        UserBaseInfoVo userBaseInfoVo = BeanCopyUtils.copyBean(user, UserBaseInfoVo.class);
        return userBaseInfoVo;
    }

}
