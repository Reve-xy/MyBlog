package cn.ly.framework.service;

import cn.ly.framework.domain.dto.LoginDto;
import cn.ly.framework.domain.vo.BlogUserLoginVo;

/**
 * 简要描述
 *
 * @author : Rêve
 * @version : 1.0
 * @date : 2023/4/6 14:12
 */
public interface BlogLoginService {

    /**
     * @param user
     * @return BlogUserLoginVo
     * @date 2023/4/6 15:13
     * @description 登录方法
     */
    BlogUserLoginVo login(LoginDto loginDto);

    /**
     * @param 
     * @return void
     * @date 2023/4/7 10:32
     * @description 退出登录
     */
    void logout();
}
