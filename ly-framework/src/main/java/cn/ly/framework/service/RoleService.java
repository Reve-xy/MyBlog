package cn.ly.framework.service;


import cn.ly.framework.domain.dto.AddRoleDto;
import cn.ly.framework.domain.dto.QueryRoleDto;
import cn.ly.framework.domain.dto.UpdateRoleDTO;
import cn.ly.framework.domain.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 角色信息表 服务类
 * </p>
 *
 * @author 刘易
 * @since 2023-04-24
 */
public interface RoleService extends IService<Role> {

    List<String> getRoleKeyByUserId(Long userId);

    List<Role> getRoleList(QueryRoleDto queryRoleDto);

    void insertRole(AddRoleDto addRoleDto);

    void updateRole(UpdateRoleDTO updateRoleDTO);

    List<Role> getAllRole();

    List<Long> getRoleIdByUserId(Long id);

    List<String> getRoleNameById(List<Long> roleIds);

}
