package cn.ly.admin.controller;



import cn.ly.framework.annotation.CacheDel;
import cn.ly.framework.annotation.SystemLog;
import cn.ly.framework.constants.RedisKeyConstants;
import cn.ly.framework.domain.dto.AddRoleDto;
import cn.ly.framework.domain.dto.QueryRoleDto;
import cn.ly.framework.domain.dto.RoleStatusDto;
import cn.ly.framework.domain.dto.UpdateRoleDTO;
import cn.ly.framework.domain.entity.Role;
import cn.ly.framework.domain.vo.PageVo;
import cn.ly.framework.domain.vo.RoleDetailsVo;
import cn.ly.framework.domain.vo.RoleVo;
import cn.ly.framework.enums.HttpCodeEnum;
import cn.ly.framework.exception.SystemException;
import cn.ly.framework.service.RoleService;
import cn.ly.framework.utils.BeanCopyUtils;
import cn.ly.framework.utils.Result;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

/**
 * 角色信息表(Role)表控制层
 *
 * @author Rêve
 * @since 2023-04-22 16:47:34
 */
@RestController
@RequestMapping("/system/role")
public class RoleController extends BaseController {
    /**
     * 服务对象
     */
    @Resource
    private RoleService roleService;

    /**
     * @param queryRoleDto
     * @return Result
     * @date 2023/4/24 21:22
     * @description 查询角色信息列表
     */
    @GetMapping("/list")
    @PreAuthorize("@ps.hasPermission('system:role:list')")
    @SystemLog("查询角色列表")
    public Result getRoleList(@Valid QueryRoleDto queryRoleDto){
        List<Role> roleList=roleService.getRoleList(queryRoleDto);
        List<RoleVo> roleVos = BeanCopyUtils.copyBeanList(roleList, RoleVo.class);
        return Result.success(new PageVo<RoleVo>(roleVos, (long) roleVos.size()));
    }

    /**
     * @param
     * @return Result
     * @date 2023/5/4 20:50
     * @description 列出所有角色
     */
    @GetMapping("/listAllRole")
    @SystemLog("列出所有角色")
    public Result listAllRole(){
        List<Role> roles=roleService.getAllRole();
        return Result.success(roles);
    }

    /**
     * @param id
     * @return Result
     * @date 2023/4/16 16:50
     * @description 角色信息回显
     */
    @GetMapping("/{id}")
    @SystemLog("角色信息回显")
    public Result getRoleInfo(@NotNull(message = "id不合法") @PathVariable Long id){
        if(Objects.isNull(id)){
            throw new SystemException(HttpCodeEnum.ILLEGAL_ID);
        }
        RoleDetailsVo roleDetailsVo = BeanCopyUtils.copyBean(roleService.getById(id), RoleDetailsVo.class);
        return Result.success(roleDetailsVo);
    }

    ///////////////////////////////////////////////////////////////////////////
    // role的crud开始
    ///////////////////////////////////////////////////////////////////////////

    /**
     * @param roleStatusDto
     * @return Result
     * @date 2023/4/25 10:46
     * @description 更改角色状态
     */
    @PutMapping("/changeStatus")
    @PreAuthorize("@ps.hasPermission('system:role:edit')")
    @SystemLog("角色状态更改")
    @CacheDel(redisPrefix = RedisKeyConstants.ROLE)
    public Result changeRoleStatus(@Valid @RequestBody RoleStatusDto roleStatusDto){
        Role role = new Role();
        role.setId(roleStatusDto.getRoleId());
        role.setStatus(roleStatusDto.getStatus());
        roleService.updateById(role);
        return Result.success();
    }

    /**
     * @param addRoleDto
     * @return Result
     * @date 2023/4/26 17:22
     * @description 添加角色
     */
    @PostMapping
    @PreAuthorize("@ps.hasPermission('system:role:add')")
    @SystemLog("添加角色")
    @CacheDel(redisPrefix = RedisKeyConstants.ROLE)
    public Result addRole(@Valid @RequestBody AddRoleDto addRoleDto){
            roleService.insertRole(addRoleDto);
            return Result.success();
    }


    /**
     * @param updateRoleDTO
     * @return Result
     * @date 2023/4/27 14:09
     * @description 更新角色信息
     */
    @PutMapping
    @PreAuthorize("@ps.hasPermission('system:role:edit')")
    @SystemLog("更新角色信息")
    @CacheDel(redisPrefix = RedisKeyConstants.ROLE)
    public Result updateRole(@Valid @RequestBody UpdateRoleDTO updateRoleDTO){
        roleService.updateRole(updateRoleDTO);
        return Result.success();
    }

    /**
     * @param ids
     * @return Result
     * @date 2023/4/27 14:09
     * @description 删除角色
     */
    @DeleteMapping
    @PreAuthorize("@ps.hasPermission('system:role:del')")
    @SystemLog("删除角色")
    @CacheDel(redisPrefix = RedisKeyConstants.ROLE)
    public Result delRoleById(@RequestBody List<String> ids){
        roleService.removeByIds(ids);
        return Result.success();
    }



}

