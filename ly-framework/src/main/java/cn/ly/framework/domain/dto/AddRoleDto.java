package cn.ly.framework.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 简要描述
 *
 * @author : Rêve
 * @version : 1.0
 * @date : 2023/4/26 17:24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddRoleDto{
    /**
     * role存储完毕设置dto中的roleid
     *
     */
    private Long id;


    /**
     * 角色名称
     */
    @NotBlank(message = "角色名称不能为空")
    private String roleName;

    /**
     * 角色权限字符串
     */
    @NotBlank(message = "角色权限不能为空")
    private String roleKey;

    /**
     * 显示顺序
     */
    @NotNull(message = "角色顺序不能为空")
    private Integer roleSort;

    /**
     * 角色状态（0正常 1停用）
     */
    @NotBlank(message = "角色状态不能为空")
    private String status;

    private Long[] menuIds;

    private String remark;

}
