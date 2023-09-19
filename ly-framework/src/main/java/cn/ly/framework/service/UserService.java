package cn.ly.framework.service;


import cn.ly.framework.domain.dto.*;
import cn.ly.framework.domain.entity.User;
import cn.ly.framework.domain.vo.UserVo;
import cn.ly.framework.utils.Result;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author 刘易
 * @since 2023-04-24
 */
public interface UserService extends IService<User> {

    List<User> getUserList(QueryUserDto queryUserDto);

    void insertUser(AddUserDto addUserDto);

    void updateUser(UpdateUserDto updateUserDto);

    UserVo getUserInfo();

    Result updateUserInfo(User user);

    Result register(RegisterDto registerDto);

    void updateLoginUser(UpdateLoginUserDto updateLoginUserDto);

    Result resetPwd(ResetPwdDto resetPwdDto);
}
