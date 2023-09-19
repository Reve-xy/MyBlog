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
 * @date : 2023/4/27 11:02
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRoleDTO{
    private Long id;

    @NotBlank(message = "角色权限不能为空")
    private String roleKey;

    @NotBlank(message = "角色状态不能为空")
    private String status;

    @NotBlank(message = "角色名称不能为空")
    private String roleName;

    @NotNull(message = "角色顺序不能为空")
    private Integer roleSort;


    private String remark;

    private Long[] menuIds;
}
