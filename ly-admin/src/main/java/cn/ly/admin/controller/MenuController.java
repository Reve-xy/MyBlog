package cn.ly.admin.controller;


import cn.hutool.core.map.MapUtil;
import cn.ly.framework.annotation.CacheDel;
import cn.ly.framework.annotation.SystemLog;
import cn.ly.framework.constants.RedisKeyConstants;
import cn.ly.framework.domain.dto.QueryMenuDto;
import cn.ly.framework.domain.entity.Menu;
import cn.ly.framework.domain.vo.*;
import cn.ly.framework.service.MenuService;
import cn.ly.framework.utils.BeanCopyUtils;
import cn.ly.framework.utils.Result;
import cn.ly.framework.utils.SecurityUtils;
import cn.ly.framework.utils.SystemConverter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 菜单权限表(Menu)表控制层
 *
 * @author Rêve
 * @since 2023-04-22 16:47:17
 */
@RestController
@RequestMapping("/system/menu")
public class MenuController extends BaseController {
    /**
     * 服务对象
     */
    @Resource
    private MenuService menuService;

    /**
     * @param 
     * @return Result
     * @date 2023/4/24 19:55
     * @description 登录后获取当前用户的路由
     */
    @GetMapping("/getRouters")
    public Result getRoutes(){
        Long userId = SecurityUtils.getUserId();
        List<MenuRouterVo> menuRouterVos=menuService.getRoutersByUserId(userId);
        return Result.success(MapUtil.builder().put("menus",menuRouterVos).build());
    }

    /**
     * @param
     * @return Result
     * @date 2023/4/26 17:19
     * @description 获取菜单树
     */
    @GetMapping("/treeSelect")
    @SystemLog("获取菜单树")
    public Result getTreeSelect(){
        //获取所有菜单
        List<Menu> menus = menuService.getMenuList(new QueryMenuDto());
        List<MenuTreeVo> options =  SystemConverter.buildMenuSelectTree(menus);
        return Result.success(options);
    }

    /**
     * @param roleId
     * @return null
     * @date 2023/5/4 20:47
     * @description 根据角色id获取菜单树
     */
    @GetMapping("/roleMenuTreeSelect/{roleId}")
    @SystemLog("获取角色菜单树")
    public Result<RoleMenuTreeSelectVo> getRoleMenuTreeSelectByRoleId(@PathVariable Long roleId){
        //获取所有菜单
        List<Menu> menus = menuService.getMenuList(new QueryMenuDto());
        //获取已激活有权限的菜单id
        List<Long> menuIdByRoleId= menuService.getRoleMenuTreeSelectByRoleId(roleId);
        //转为菜单树
        List<MenuTreeVo> menuTreeVos = SystemConverter.buildMenuSelectTree(menus);
        RoleMenuTreeSelectVo roleMenuTreeSelectVo=new RoleMenuTreeSelectVo(menuIdByRoleId,menuTreeVos);
        return Result.success(roleMenuTreeSelectVo);
    }



    /**
     * @param queryMenuDto
     * @return Result
     * @date 2023/4/24 20:09
     * @description 需要展示菜单列表，不需要分页。可以针对菜单名进行模糊查询,也可以针对菜单的状态进行查询。菜单要按照父菜单id和orderNum进行排序
     */
    @GetMapping("/list")
    @PreAuthorize("@ps.hasPermission('system:menu:list')")
    @SystemLog("获取菜单列表")
    public Result getMenuList(QueryMenuDto queryMenuDto){
        List<Menu>  menuList= menuService.getMenuList(queryMenuDto);
        List<MenuVo> menuVos = BeanCopyUtils.copyBeanList(menuList, MenuVo.class);
        return Result.success(menuVos);
    }

    /**
     * @param menuId
     * @return Result
     * @date 2023/4/24 20:28
     * @description 根据菜单编号获取详细信息
     */
    @GetMapping(value = "/{menuId}")
    @SystemLog("获取菜单详细信息")
    public Result getInfo(@PathVariable Long menuId) {
        Menu menu = menuService.getById(menuId);
        MenuDetailsVo menuDetailsVo = BeanCopyUtils.copyBean(menu, MenuDetailsVo.class);
        return Result.success(menuDetailsVo);
    }

    ///////////////////////////////////////////////////////////////////////////
    // menu的crud开始
    ///////////////////////////////////////////////////////////////////////////
    /**
     * @param menu
     * @return Result
     * @date 2023/4/24 20:28
     * @description 新增菜单
     */
    // TODO: 2023/4/24 dto没写，应该要做数据校验的，不能传空的过来
    @PostMapping
    @PreAuthorize("@ps.hasPermission('system:menu:add')")
    @SystemLog("新增菜单")
    @CacheDel(redisPrefix = RedisKeyConstants.MENU)
    public Result addMenu(@RequestBody Menu menu){
        menuService.save(menu);
        return Result.success();
    }

    /**
     * @param menu
     * @return Result
     * @date 2023/4/24 20:39
     * @description 修改时，上级菜单不应该为自己
     */
    @PutMapping
    @PreAuthorize("@ps.hasPermission('system:menu:edit')")
    @SystemLog("修改菜单")
    @CacheDel(redisPrefix = RedisKeyConstants.MENU)
    public Result updateMenu(@RequestBody Menu menu){
        if (menu.getId().equals(menu.getParentId())) {
            return Result.failure(500,"修改菜单'" + menu.getMenuName() + "!失败，上级菜单不能选择自己");
        }
        menuService.updateById(menu);
        return Result.success();
    }


    @DeleteMapping("/{menuId}")
    @PreAuthorize("@ps.hasPermission('system:menu:del')")
    @SystemLog("删除菜单")
    @CacheDel(redisPrefix = RedisKeyConstants.MENU)
    public Result delMenu(@PathVariable Long menuId){
        menuService.delMenu(menuId);
        return Result.success();
    }
}

