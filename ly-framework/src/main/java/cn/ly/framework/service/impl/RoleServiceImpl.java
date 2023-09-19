package cn.ly.framework.service.impl;


import cn.ly.framework.annotation.CacheFind;
import cn.ly.framework.constants.RedisKeyConstants;
import cn.ly.framework.constants.SystemConstants;
import cn.ly.framework.domain.dto.AddRoleDto;
import cn.ly.framework.domain.dto.QueryRoleDto;
import cn.ly.framework.domain.dto.UpdateRoleDTO;
import cn.ly.framework.domain.entity.Role;
import cn.ly.framework.domain.entity.RoleMenu;
import cn.ly.framework.domain.entity.UserRole;
import cn.ly.framework.mapper.RoleMapper;
import cn.ly.framework.service.RoleMenuService;
import cn.ly.framework.service.RoleService;
import cn.ly.framework.service.UserRoleService;
import cn.ly.framework.utils.BeanCopyUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色信息表 服务实现类
 * </p>
 *
 * @author 刘易
 * @since 2023-04-24
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    /**
     * @param userId
     * @return List<String>
     * @date 2023/4/27 17:27
     * @description 获取角色名称
     */
    @Override
    @CacheFind(key = RedisKeyConstants.ROLE_KEY)
    public List<String> getRoleKeyByUserId(Long userId) {
        if(SystemConstants.ADMIN_ID.equals(userId)){
            List<String> adminRole=new ArrayList<>();
            adminRole.add("admin");
            return adminRole;
        }
        return baseMapper.getRolesByUserId(userId);
    }

    /**
     * @param queryRoleDto
     * @return List<Role>
     * @date 2023/4/24 21:14
     * @description 分页查询角色表
     */
    @Override
    @CacheFind(key = RedisKeyConstants.ROLE_LIST_KEY)
    public List<Role> getRoleList(QueryRoleDto queryRoleDto) {
        String roleName = queryRoleDto.getRoleName();
        String status = queryRoleDto.getStatus();

        LambdaQueryWrapper<Role> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(StringUtils.hasText(status),Role::getStatus,status).like(StringUtils.hasText(roleName),Role::getRoleName,roleName);

        PageHelper.startPage(queryRoleDto.getPageNum(),queryRoleDto.getPageSize());
        List<Role> roleList = list(lambdaQueryWrapper);
        PageInfo<Role> rolePageInfo = new PageInfo<>(roleList);
        List<Role> roles = rolePageInfo.getList();
        return roles ;
    }

    /**
     * @param addRoleDto
     * @return void
     * @date 2023/4/27 10:22
     * @description 添加角色
     */
    @Override
    public void insertRole(AddRoleDto addRoleDto) {
        Role role = BeanCopyUtils.copyBean(addRoleDto, Role.class);
        save(role);
        if(role.getMenuIds()!=null&&role.getMenuIds().length>0){
            insertRoleMenu(role);
        }
    }

    /**
     * @param updateRoleDTO
     * @return void
     * @date 2023/4/27 11:05
     * @description 修改角色信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRole(UpdateRoleDTO updateRoleDTO) {
        Role role = BeanCopyUtils.copyBean(updateRoleDTO, Role.class);
        // 删除用户与角色关联
        LambdaQueryWrapper<RoleMenu> roleMenuUpdateWrapper = new LambdaQueryWrapper<>();
        roleMenuUpdateWrapper.eq(RoleMenu::getRoleId,role.getId());
        roleMenuService.remove(roleMenuUpdateWrapper);
        updateById(role);
        if(role.getMenuIds()!=null&&role.getMenuIds().length>0){
            insertRoleMenu(role);
        }
    }

    /**
     * @param
     * @return List<Role>
     * @date 2023/4/27 15:11
     * @description 获取所有状态正常的角色
     */
    @Override
    @CacheFind(key = RedisKeyConstants.ROLE_STATUS_KEY)
    public List<Role> getAllRole() {
        return list(Wrappers.<Role>lambdaQuery().eq(Role::getStatus, SystemConstants.NORMAL));
    }

    /**
     * @param id
     * @return List<Long>
     * @date 2023/4/27 17:23
     * @description 根据用户Id获取对应的角色Id
     */
    @Override
    public List<Long> getRoleIdByUserId(Long id) {
        return baseMapper.selectRoleIdByUserId(id);
    }

    /**
     * @param roleIds
     * @return List<String>
     * @date 2023/5/6 23:50
     * @description 根据角色id获取角色名称
     */
    @Override
    public List<String> getRoleNameById(List<Long> roleIds) {
        List<Role> roles = getBaseMapper().selectBatchIds(roleIds);
        return roles.stream().map(r -> r.getRoleName()).collect(Collectors.toList());
    }

    @Resource
    RoleMenuService roleMenuService;

    /**
     * @param role
     * @return void
     * @date 2023/4/16 22:17
     * @description 新增role与menu数据
     */
    private void insertRoleMenu(Role role) {
        List<RoleMenu> roleMenuList = Arrays.stream(role.getMenuIds())
                .map(menuId -> new RoleMenu(role.getId(), menuId))
                .collect(Collectors.toList());
        roleMenuService.saveBatch(roleMenuList);
    }

}
