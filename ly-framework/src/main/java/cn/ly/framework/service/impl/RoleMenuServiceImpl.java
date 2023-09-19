package cn.ly.framework.service.impl;


import cn.ly.framework.domain.entity.RoleMenu;
import cn.ly.framework.mapper.RoleMenuMapper;
import cn.ly.framework.service.RoleMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色和菜单关联表 服务实现类
 * </p>
 *
 * @author 刘易
 * @since 2023-04-22
 */
@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {

}
