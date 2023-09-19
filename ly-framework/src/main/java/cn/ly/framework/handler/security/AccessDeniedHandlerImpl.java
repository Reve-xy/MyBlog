package cn.ly.framework.handler.security;

import cn.ly.framework.enums.HttpCodeEnum;
import cn.ly.framework.utils.WebUtils;
import cn.ly.framework.utils.Result;
import com.alibaba.fastjson.JSON;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 简要描述
 * 授权失败的异常
 * @author : Rêve
 * @version : 1.0
 * @date : 2023/4/7 9:43
 */
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        e.printStackTrace();
        Result result = Result.failure(HttpCodeEnum.NO_OPERATOR_AUTH);
        //响应给前端
        WebUtils.renderString(response, JSON.toJSONString(result));
    }
}
