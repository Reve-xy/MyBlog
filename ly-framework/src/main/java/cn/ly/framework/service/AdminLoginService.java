package cn.ly.framework.service;

import cn.ly.framework.domain.dto.LoginDto;
import cn.ly.framework.domain.dto.RegisterDto;
import cn.ly.framework.domain.dto.UserDto;
import cn.ly.framework.domain.vo.UserBaseInfoVo;
import cn.ly.framework.utils.Result;

/**
 * 简要描述
 *
 * @author : Rêve
 * @version : 1.0
 * @date : 2023/4/14 15:27
 */
public interface AdminLoginService {
    public void register(RegisterDto registerDto);
    public String login(LoginDto loginDto);
    public UserBaseInfoVo getUserInfoById(Long userId);

}
