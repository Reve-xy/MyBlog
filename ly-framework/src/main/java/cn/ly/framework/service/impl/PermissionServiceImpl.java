package cn.ly.framework.service.impl;

import cn.ly.framework.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 简要描述
 *
 * @author : Rêve
 * @version : 1.0
 * @date : 2023/5/11 22:43
 */
@Service("ps")
public class PermissionServiceImpl{
    /**
     * @param permission
     * @return boolean
     * @date 2023/5/11 22:45
     * @description 判断用户是否有权限
     */
    public boolean hasPermission(String permission) {
        //如果是超级管理员  直接返回true
        if(SecurityUtils.isAdmin()){
            return true;
        }
        //否则  获取当前登录用户所具有的权限列表 如何判断是否存在permission
        List<String> permissions = SecurityUtils.getLoginUser().getPermissions();
        return permissions.contains(permission);
    }
}
