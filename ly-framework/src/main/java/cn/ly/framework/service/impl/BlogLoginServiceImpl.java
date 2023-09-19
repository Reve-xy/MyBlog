package cn.ly.framework.service.impl;

import cn.ly.framework.config.ReveConfig;
import cn.ly.framework.constants.RedisKeyConstants;
import cn.ly.framework.domain.dto.LoginDto;
import cn.ly.framework.domain.entity.LoginUser;
import cn.ly.framework.domain.vo.BlogUserLoginVo;
import cn.ly.framework.domain.vo.UserInfoVo;
import cn.ly.framework.enums.HttpCodeEnum;
import cn.ly.framework.exception.SystemException;
import cn.ly.framework.service.BlogLoginService;
import cn.ly.framework.service.CaptchaService;
import cn.ly.framework.utils.JwtUtils;
import cn.ly.framework.utils.BeanCopyUtils;
import cn.ly.framework.utils.RedisCache;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import javax.annotation.Resource;
import java.util.Objects;

/**
 * 简要描述
 *
 * @author : Rêve
 * @version : 1.0
 * @date : 2023/4/6 14:13
 */
@Service
public class BlogLoginServiceImpl implements BlogLoginService {

    //需要进行配置
    @Resource
    AuthenticationManager authenticationManager;
    @Resource
    RedisCache redisCache;

    @Resource
    ReveConfig reveConfig;

    @Resource
    CaptchaService captchaService;
    @Resource
    JwtUtils jwtUtils;
    @Override
    public BlogUserLoginVo login(LoginDto loginDto) {
        String userName = loginDto.getUserName();
        if(!StringUtils.hasText(userName)){
            throw new SystemException(HttpCodeEnum.REQUIRE_ACCOUNT);
        }
        if(reveConfig.getCaptchaEnabled()){
            captchaService.verifyCaptcha(loginDto.getUuid(),loginDto.getCaptcha());
        }
        //传入用户需要认证的信息
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=new UsernamePasswordAuthenticationToken(userName,loginDto.getPassword());
        //对用户信息进行验证
        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        //判断是否认证通过
        if(Objects.isNull(authenticate)){
            throw new SystemException(HttpCodeEnum.LOGIN_ERROR);
        }

        //获取userid 生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = jwtUtils.createJWT(userId);
        //把用户信息存入redis
        redisCache.setCacheObject(RedisKeyConstants.BLOG_LOGIN_KEY +userId,loginUser);

        //把token和userinfo封装 返回
        //把User转换成UserInfoVo
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
        BlogUserLoginVo vo = new BlogUserLoginVo(jwt,userInfoVo);
        return vo;
    }

    @Override
    public void logout() {
        //从security上下文获取到当前用户的身份
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();

        Long id = loginUser.getUser().getId();

        redisCache.deleteObject(RedisKeyConstants.BLOG_LOGIN_KEY+id);
        return;
    }
}
