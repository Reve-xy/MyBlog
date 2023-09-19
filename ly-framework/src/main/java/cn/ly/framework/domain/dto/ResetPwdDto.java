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
 * @date : 2023/6/14 11:02
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResetPwdDto {
    @NotBlank(message="邮箱不能为空")
    @Pattern(regexp= RegexValidConstants.EMAIL_REGEX,message = "请输入正确的邮件地址")
    private String email;
    @NotBlank(message="密码不能为空")
    // @Pattern(regexp = RegexValidConstants.PWD_REGEX,message = "密码不合法")
    private String password;
    @NotBlank(message="确认密码不能为空")
    private String confirmPassword;
}
