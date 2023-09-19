package cn.ly.framework.service.impl;


import cn.ly.framework.constants.SystemConstants;
import cn.ly.framework.domain.entity.LoginUser;
import cn.ly.framework.domain.entity.User;
import cn.ly.framework.enums.HttpCodeEnum;
import cn.ly.framework.exception.SystemException;
import cn.ly.framework.mapper.MenuMapper;
import cn.ly.framework.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * 简要描述
 *
 * @author : Rêve
 * @version : 1.0
 * @date : 2023/4/23 15:32
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    UserMapper userMapper;

    @Resource
    MenuMapper menuMapper;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user;
        if(userName.contains("@")){
            user   = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getEmail, userName));

        }else{
            user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUserName, userName));
        }
        if(Objects.isNull(user)){
            throw new SystemException(HttpCodeEnum.USER_NOT_FOUND);
        }else if(!SystemConstants.NORMAL.equals(user.getStatus())){
            throw new SystemException(HttpCodeEnum.USER_DISABLED);
        }
        if(user.getId()==1){
            return new LoginUser(user,null);
        }
        else if(user.getType().equals(SystemConstants.ADMIN_ID.toString())){
            List<String> adminPerms = menuMapper.getPermsByUserId(user.getId());
            return new LoginUser(user,adminPerms);
        }
        return new LoginUser(user,null);
    }
}
