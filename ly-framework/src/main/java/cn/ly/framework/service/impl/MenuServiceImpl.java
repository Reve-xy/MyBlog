package cn.ly.framework.service.impl;


import cn.ly.framework.annotation.CacheFind;
import cn.ly.framework.constants.RedisExprieConstants;
import cn.ly.framework.constants.RedisKeyConstants;
import cn.ly.framework.constants.SystemConstants;
import cn.ly.framework.domain.dto.QueryMenuDto;
import cn.ly.framework.domain.entity.Menu;
import cn.ly.framework.domain.vo.MenuRouterVo;
import cn.ly.framework.enums.HttpCodeEnum;
import cn.ly.framework.exception.SystemException;
import cn.ly.framework.mapper.MenuMapper;
import cn.ly.framework.service.MenuService;
import cn.ly.framework.utils.BeanCopyUtils;
import cn.ly.framework.utils.RedisCache;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单权限表 服务实现类
 * </p>
 *
 * @author 刘易
 * @since 2023-04-24
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Resource
    RedisCache redisCache;

    /**
     * @param userId
     * @return List<MenuRouterVo>
     * @date 2023/4/24 15:19
     * @description 根据当前登录的用户id获取路由信息
     */
    @Override
    @CacheFind(key= RedisKeyConstants.ROLE_MENU_ROUTES,expire = RedisExprieConstants.USER_ROUTES_EXPIRE)
    public List<MenuRouterVo> getRoutersByUserId(Long userId) {
        List<Menu> menus = null;
        if (SystemConstants.ADMIN_ID.equals(userId)) {
            menus = baseMapper.selectAllRouterMenu();
        } else {
            menus = baseMapper.selectRouterMenuTreeByUserId(userId);
        }
        List<MenuRouterVo> menuRouterVo = BeanCopyUtils.copyBeanList(menus, MenuRouterVo.class);
        List<MenuRouterVo> menuTreeList = buildMenuTree(menuRouterVo, SystemConstants.MENU_ROOT_ID);
        return menuTreeList;
    }

    /**
     * @param menus
     * @return List<Menu>
     * @date 2023/4/24 15:21
     * @description 构建路由树
     */
    private List<MenuRouterVo> buildMenuTree(List<MenuRouterVo> menus, Long rootId) {
        List<MenuRouterVo> menuList = menus.stream().filter(menu -> menu.getParentId().equals(rootId))
                .map(menu -> menu.setChildren(getChildren(menu, menus)))
                .collect(Collectors.toList());
        return menuList;
    }

    private List<MenuRouterVo> getChildren(MenuRouterVo menu, List<MenuRouterVo> menus) {
        return menus.stream().filter(m -> m.getParentId().equals(menu.getId()))
                .map(m -> m.setChildren(getChildren(m, menus)))
                .collect(Collectors.toList());
    }

    /**
     * @param userId
     * @return List<String>
     * @date 2023/4/24 14:26
     * @description 获取用户权限信息perms
     */
    @Override
    @CacheFind(key=RedisKeyConstants.ROLE_MENU_PERMS,expire = RedisExprieConstants.USER_PERMISSIONS_EXPIRE)
    public List<String> getPermsByUserId(Long userId) {
        List permsList;
        if (SystemConstants.ADMIN_ID.equals(userId)) {
            LambdaQueryWrapper<Menu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            // 菜单类型需为菜单或者按钮
            lambdaQueryWrapper.in(Menu::getMenuType, SystemConstants.MENU, SystemConstants.BUTTON);
            // 菜单为停用，正常状态
            lambdaQueryWrapper.eq(Menu::getStatus, SystemConstants.STATUS_NORMAL);
            // 只取perms字段
            lambdaQueryWrapper.select(Menu::getPerms);
            List<Menu> menus = list(lambdaQueryWrapper);
            permsList = menus.stream().map(Menu::getPerms).collect(Collectors.toList());
        }else{
            // 不然就进行级联查询
            permsList=baseMapper.getPermsByUserId(userId);
        }
        return permsList;
    }

    /**
     * @param queryMenuDto
     * @return List<Menu>
     * @date 2023/4/24 20:47
     * @description 菜单页面，获取菜单列表
     */
    @Override
    @CacheFind(key = RedisKeyConstants.MENU_LIST_KEY)
    public List<Menu> getMenuList(QueryMenuDto queryMenuDto) {
        String menuName = queryMenuDto.getMenuName();
        String status = queryMenuDto.getStatus();
        LambdaQueryWrapper<Menu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(StringUtils.hasText(status),Menu::getStatus,status).like(StringUtils.hasText(menuName),Menu::getMenuName,menuName);
        lambdaQueryWrapper.orderByAsc(Menu::getParentId,Menu::getOrderNum);
        List<Menu> menuList = list(lambdaQueryWrapper);
        return menuList;
    }
    
    /**
     * @param menuId
     * @return void
     * @date 2023/4/24 20:46
     * @description 逻辑删除菜单
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delMenu(Long menuId) {
        LambdaQueryWrapper<Menu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Menu::getParentId,menuId);
        int count = count(lambdaQueryWrapper);
        if(count!=0){
            throw new SystemException(HttpCodeEnum.HAS_CHILD_MENU);
        }
        removeById(menuId);
    }

    /**
     * @param id
     * @return Result
     * @date 2023/4/16 16:56
     * @description 根据RoleId获取角色已激活菜单
     */
    @Override
    @CacheFind(key = RedisKeyConstants.ROLE_ACT_MENU)
    public List<Long> getRoleMenuTreeSelectByRoleId(Long id) {
        if(SystemConstants.ADMIN_ID.equals(id)){
            LambdaQueryWrapper<Menu> lambdaQueryWrapper=new LambdaQueryWrapper<>();
            lambdaQueryWrapper.select(Menu::getId).orderByAsc(Menu::getParentId,Menu::getOrderNum);
            List<Menu> list = list(lambdaQueryWrapper);
            List<Long> list1 = list.stream().map(m -> m.getId()).collect(Collectors.toList());
            return list1;
        }
        return getBaseMapper().selectMenuListByRoleId(id);
    }
}
