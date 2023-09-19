package cn.ly.framework.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 简要描述
 * 角色状态Dto
 * @author : Rêve
 * @version : 1.0
 * @date : 2023/4/25 10:39
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleStatusDto {
    @NotNull(message = "角色不能为空")
    private Long roleId;
    @NotBlank(message = "角色不能为空")
    private String status;
}
