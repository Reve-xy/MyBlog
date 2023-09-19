package cn.ly.framework.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 简要描述
 *
 * @author : Rêve
 * @version : 1.0
 * @date : 2023/4/27 10:39
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDetailsVo {
    private Long id;
    private String roleKey;
    private String status;
    private String roleName;
    private Integer roleSort;
    private String remark;
}
