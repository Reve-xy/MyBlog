package cn.ly.framework.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 简要描述
 * 用户基本信息(UserVo)+角色权限信息
 * @author : Rêve
 * @version : 1.0
 * @date : 2023/4/23 21:51
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoVo {
    private UserBaseInfoVo user;
    private List<String> permissions;
    private List<String> roles;

}
