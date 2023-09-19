package cn.ly.framework.service;


import cn.ly.framework.domain.dto.QueryMenuDto;
import cn.ly.framework.domain.entity.Menu;
import cn.ly.framework.domain.vo.MenuRouterVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 菜单权限表 服务类
 * </p>
 *
 * @author 刘易
 * @since 2023-04-24
 */
public interface MenuService extends IService<Menu> {

    List<MenuRouterVo> getRoutersByUserId(Long userId);

    List<String> getPermsByUserId(Long userId);

    List<Menu>  getMenuList(QueryMenuDto queryMenuDto);

    void delMenu(Long menuId);

    List<Long> getRoleMenuTreeSelectByRoleId(Long roleId);
}
