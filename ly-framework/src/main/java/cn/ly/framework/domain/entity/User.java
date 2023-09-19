package cn.ly.framework.domain.entity;

import cn.ly.framework.constants.ParamVaildConstants;
import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author 刘易
 * @since 2023-04-06
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_user")
public class User extends BaseEntity implements Serializable{

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Null(message = ParamVaildConstants.idNull)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    @Pattern(regexp = "//^[a-zA-Z0-9_-]{4,16}$//",message = ParamVaildConstants.userNameVaild)
    @TableField("user_name")
    private String userName;

    /**
     * 昵称
     */
    @TableField("nick_name")
    private String nickName;

    /**
     * 密码
     */
    @TableField("password")
    private String password;

    /**
     * 用户类型：0代表普通用户，1代表管理员
     */
    @TableField("type")
    private String type;

    /**
     * 账号状态（0正常 1停用）
     */
    @TableField("status")
    private String status;

    /**
     * 邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 手机号
     */
    @TableField("phoneNumber")
    private String phoneNumber;

    /**
     * 用户性别（0男，1女，2未知）
     */
    @TableField("sex")
    private String sex;

    /**
     * 头像
     */
    @TableField("avatar")
    private String avatar;

    /**
     * 删除标志（0代表未删除，1代表已删除）
     */
    @TableField("del_flag")
    private Integer delFlag;

    @TableField(exist = false)
    Long[] roleIds;
}
