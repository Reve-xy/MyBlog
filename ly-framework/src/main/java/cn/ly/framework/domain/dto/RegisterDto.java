package cn.ly.framework.domain.dto;

import cn.ly.framework.constants.RegexValidConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 简要描述
 * 注册Dto
 * @author : Rêve
 * @version : 1.0
 * @date : 2023/4/23 19:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto {
    @NotBlank(message = "用户名不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9_-]{2,19}$",message = "请输入正确的用户名")
    private String userName;

    @NotBlank(message = "昵称不能为空")
    @Pattern(regexp = RegexValidConstants.NICK_NAME_REGEX,message = "昵称格式不正确")
    private String nickName;

    @NotBlank(message = "密码不能为空")
    // @Pattern(regexp = RegexValidConstants.PWD_REGEX,message = "请输入正确的密码")
    private String password;

    @NotBlank(message = "确认密码不能为空")
    // @Pattern(regexp = RegexValidConstants.PWD_REGEX,message = "请输入正确的密码")
    private String confirmPassword;

    private String sex;

    @NotBlank(message = "邮箱不能为空")
    @Pattern(regexp = RegexValidConstants.EMAIL_REGEX,message = "请输入正确的邮箱")
    private String email;

    @NotBlank(message = "验证码不能为空")
    private String captcha;
}
