package cn.ly.framework.mapper;


import cn.ly.framework.domain.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 角色信息表 Mapper 接口
 * </p>
 *
 * @author 刘易
 * @since 2023-04-24
 */
public interface RoleMapper extends BaseMapper<Role> {

    List<String> getRolesByUserId(Long userId);

    List<Long> selectRoleIdByUserId(Long userId);
}
