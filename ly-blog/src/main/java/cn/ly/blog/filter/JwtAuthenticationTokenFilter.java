package cn.ly.blog.filter;

import cn.hutool.jwt.JWTUtil;
import cn.ly.framework.constants.RedisKeyConstants;
import cn.ly.framework.constants.SystemConstants;
import cn.ly.framework.domain.entity.LoginUser;
import cn.ly.framework.domain.entity.User;
import cn.ly.framework.enums.HttpCodeEnum;
import cn.ly.framework.service.UserService;
import cn.ly.framework.utils.JwtUtils;
import cn.ly.framework.utils.RedisCache;
import cn.ly.framework.utils.Result;
import cn.ly.framework.utils.WebUtils;
import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * 简要描述
 *
 * @author : Rêve
 * @version : 1.0
 * @date : 2023/4/6 15:31
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Resource
    RedisCache redisCache;

    @Resource
    JwtUtils jwtUtils;

    @Resource
    UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader(jwtUtils.getHeader());
        if (!StringUtils.hasText(token)) {
            // 说明该接口不需要登录  直接放行
            filterChain.doFilter(request, response);
            return;
        }
        Claims claims = null;
        try {
            claims = jwtUtils.parseJWT(token);
        } catch (Exception e) {
            e.printStackTrace();
            // token超时  token非法
            // 响应告诉前端需要重新登录
            Result result = Result.failure(HttpCodeEnum.NEED_LOGIN);
            WebUtils.renderString(response, JSON.toJSONString(result));
            return;
        }

        String userId = claims.getSubject();
        LoginUser loginUser =(LoginUser) redisCache.getCacheObject(RedisKeyConstants.BLOG_LOGIN_KEY + userId);
        User byId = userService.getById(userId);
        if(Objects.nonNull(byId)&&!byId.getStatus().equals(SystemConstants.STATUS_NORMAL)){
            Result result = Result.failure(HttpCodeEnum.USER_DISABLED);
            WebUtils.renderString(response, JSON.toJSONString(result));
            return;
        }
        if(Objects.isNull(loginUser)){
            //token合法，但是可能已经退出，缓存中无法找到对应用户
            Result result=Result.failure(HttpCodeEnum.NEED_LOGIN);
            WebUtils.renderString(response, JSON.toJSONString(result));
            return;
        }

        //验证无误，存入security上下文，防止被后续过滤器拦截
        //想认证成功，必须在usernamepasswordtoken中传入三个参数
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser,null,null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }
}
