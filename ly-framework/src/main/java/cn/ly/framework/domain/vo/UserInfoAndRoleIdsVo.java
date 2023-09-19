package cn.ly.framework.domain.vo;

import cn.ly.framework.domain.entity.Role;
import cn.ly.framework.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 简要描述
 *
 * @author : Rêve
 * @version : 1.0
 * @date : 2023/4/27 17:17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoAndRoleIdsVo {
    private UserVo user;
    private List<Role> roles;
    private List<Long> roleIds;
}
