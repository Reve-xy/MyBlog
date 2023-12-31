package cn.ly.framework.utils;


import cn.ly.framework.constants.SystemConstants;
import cn.ly.framework.domain.entity.User;
import cn.ly.framework.enums.HttpCodeEnum;
import cn.ly.framework.exception.SystemException;
import cn.ly.framework.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * 简要描述
 *
 * @author : Rêve
 * @version : 1.0
 * @date : 2023/5/6 22:06
 */
@Component
@Data
@Accessors(chain = true)
public class CheckRepeatUtils {

    @Resource
    UserMapper userMapper;

    String type;

    public CheckRepeatUtils checkUserNameRepeat(User user){
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(User::getUserName);
        lambdaQueryWrapper.eq(User::getUserName, user.getUserName());
        if(StringUtils.hasText(type)&& SystemConstants.UPDATE.equals(type)){
            lambdaQueryWrapper.ne(User::getId, user.getId());
        }
        List<User> users = userMapper.selectList(lambdaQueryWrapper);
        if(users!=null&&users.size()>0){
            throw new SystemException(HttpCodeEnum.USERNAME_EXIST);
        }
        return this;
    }

    public CheckRepeatUtils checkNickNameRepeat(User user){
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(User::getNickName);
        lambdaQueryWrapper.eq(User::getNickName, user.getNickName());
        if(StringUtils.hasText(type)&&SystemConstants.UPDATE.equals(type)){
            lambdaQueryWrapper.ne(User::getId, user.getId());
        }
        List<User> users = userMapper.selectList(lambdaQueryWrapper);
        if(users!=null&&users.size()>0){
            throw new SystemException(HttpCodeEnum.NICKNAME_EXIST);
        }
        return this;
    }

    public CheckRepeatUtils checkEmailRepeat(User user){
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(User::getEmail);
        lambdaQueryWrapper.eq(User::getEmail, user.getEmail());
        if(StringUtils.hasText(type)&&SystemConstants.UPDATE.equals(type)){
            lambdaQueryWrapper.ne(User::getId, user.getId());
        }
        List<User> users = userMapper.selectList(lambdaQueryWrapper);
        if(users!=null&&users.size()>0){
            throw new SystemException(HttpCodeEnum.EMAIL_EXIST);
        }
        return this;
    }

    public CheckRepeatUtils checkPhoneNumberRepeat(User user){
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(User::getPhoneNumber);
        lambdaQueryWrapper.eq(User::getPhoneNumber, user.getPhoneNumber());
        if(StringUtils.hasText(type)&&SystemConstants.UPDATE.equals(type)){
            lambdaQueryWrapper.ne(User::getId, user.getId());
        }
        List<User> users = userMapper.selectList(lambdaQueryWrapper);
        if(users!=null&&users.size()>0){
            throw new SystemException(HttpCodeEnum.PHONE_EXIST);
        }
        return this;
    }
}
