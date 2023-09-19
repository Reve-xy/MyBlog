package cn.ly.framework.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * 简要描述
 * 查询角色
 * @author : Rêve
 * @version : 1.0
 * @date : 2023/4/24 21:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryRoleDto {

    @NotNull(message = "页码不能为空")
    Integer pageNum;

    @NotNull(message = "每页条数不能为空")
    Integer pageSize;

    private String  roleName;
    private String status;

}
