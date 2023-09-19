package cn.ly.framework.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 简要描述
 * 用户表需要的数据
 * @author : Rêve
 * @version : 1.0
 * @date : 2023/4/27 14:51
 */
@Data
@Accessors(chain=true)
@AllArgsConstructor
@NoArgsConstructor
public class UserVo {

    private Long id;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 昵称
     */
    private String nickName;

    private String status;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 用户性别（0男，1女，2未知）
     */
    private String sex;

    private String type;

    /**
     * 头像
     */
    private String avatar;
    private Long updateBy;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private List<String> roleGroups;
}
