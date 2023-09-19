package cn.ly.framework.domain.dto;

import cn.ly.framework.constants.RegexValidConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 简要描述
 *
 * @author : Rêve
 * @version : 1.0
 * @date : 2023/4/27 15:15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddUserDto{
    @NotBlank(message = "昵称不能为空")
    private String nickName;

    @NotBlank(message = "用户名不能为空")
    // @Pattern(regexp = "^[a-zA-Z0-9_-]{5,19}$",message = "请输入正确的用户名")
    private String userName;

    @NotBlank(message = "状态不能为空")
    private String status;
    /**
     * @description 性别选填
     */
    private String sex;

    @NotBlank(message = "邮箱不能为空")
    @Pattern(regexp = RegexValidConstants.EMAIL_REGEX,message = "请输入正确的邮箱")
    private String email;

    @NotBlank(message = "电话号码不能为空")
    // @Pattern(regexp = RegexValidConstants.PHONE_REGEX,message = "请输入正确的手机号")
    private String phoneNumber;

    @NotBlank(message = "密码不能为空")
    // @Pattern(regexp = RegexValidConstants.PWD_REGEX,message = "请输入合法的密码")
    private String password;

   private Long[] roleIds;
}
