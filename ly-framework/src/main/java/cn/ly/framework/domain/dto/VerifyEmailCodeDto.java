package cn.ly.framework.domain.dto;

import cn.ly.framework.constants.RegexValidConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 简要描述
 *
 * @author : Rêve
 * @version : 1.0
 * @date : 2023/5/25 14:50
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerifyEmailCodeDto {
    @NotBlank(message = "验证码不能为空")
    private String captcha;
    @NotBlank(message = "邮箱不能为空")
    @Pattern(regexp= RegexValidConstants.EMAIL_REGEX,message = "请输入正确的邮件地址")
    private String email;
}
