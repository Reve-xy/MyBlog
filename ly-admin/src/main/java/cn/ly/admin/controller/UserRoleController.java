package cn.ly.admin.controller;


import cn.ly.framework.service.UserRoleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 用户和角色关联表(UserRole)表控制层
 *
 * @author Rêve
 * @since 2023-04-22 16:47:53
 */
@RestController
@RequestMapping("/userRole")
public class UserRoleController extends BaseController {
    /**
     * 服务对象
     */
    @Resource
    private UserRoleService userRoleService;
}

