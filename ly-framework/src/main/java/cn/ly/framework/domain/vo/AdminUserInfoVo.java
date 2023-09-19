package cn.ly.framework.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 简要描述
 *
 * @author : Rêve
 * @version : 1.0
 * @date : 2023/4/15 14:28
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserInfoVo {

    /*
    * 权限集合
    * */
    private List<String> permissions;
    /*
     * 身份集合
     * */
    private List<String> roles;
    /*
     * 用户信息
     * */
    private UserInfoVo user;
}