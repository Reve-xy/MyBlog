package cn.ly.admin.controller;


import cn.ly.framework.service.RoleMenuService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 角色和菜单关联表(RoleMenu)表控制层
 *
 * @author Rêve
 * @since 2023-04-22 16:47:41
 */
@RestController
@RequestMapping("/roleMenu")
public class RoleMenuController extends BaseController {
    /**
     * 服务对象
     */
    @Resource
    private RoleMenuService roleMenuService;
}

