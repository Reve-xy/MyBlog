package cn.ly.framework.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 简要描述
 * 角色表所需的vo
 * @author : Rêve
 * @version : 1.0
 * @date : 2023/4/16 15:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleVo {
    private Long id;
    private String roleKey;
    private String status;
    private String roleName;
    private LocalDateTime createTime;
    private Integer roleSort;

}
