package cn.ly.framework.mapper;

import cn.ly.framework.domain.entity.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 菜单权限表 Mapper 接口
 * </p>
 *
 * @author 刘易
 * @since 2023-04-15
 */
public interface MenuMapper extends BaseMapper<Menu> {

    List<String> getPermsByUserId(Long id);

    List<Menu> selectAllRouterMenu();
    List<Menu>  selectRouterMenuTreeByUserId(Long id);

    List<Long> selectMenuListByRoleId(Long id);
}
