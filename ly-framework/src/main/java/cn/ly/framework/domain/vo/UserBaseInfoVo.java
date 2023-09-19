package cn.ly.framework.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 简要描述
 * 返回视图的用户基本信息
 * @author : Rêve
 * @version : 1.0
 * @date : 2023/4/23 21:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserBaseInfoVo {
    private Long id;
    private String nickName;
    private String sex;
    private String email;
    private String avatar;
    private String phoneNumber;
}
