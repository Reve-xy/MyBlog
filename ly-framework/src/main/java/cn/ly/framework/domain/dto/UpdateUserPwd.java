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
 * @date : 2023/5/11 20:57
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateUserPwd {
    @NotBlank(message="密码不能为空")
    @Pattern(regexp = RegexValidConstants.PWD_REGEX,message = "密码不合法")
    private String oldPassword;
    @NotBlank(message="新密码不能为空")
    @Pattern(regexp = RegexValidConstants.PWD_REGEX,message = "密码不合法")
    private String newPassword;
    @NotBlank(message="确认密码不能为空")
    private String confirmPassword;
}
