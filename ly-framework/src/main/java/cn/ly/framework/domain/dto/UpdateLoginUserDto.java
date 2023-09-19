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
 * @date : 2023/6/9 14:58
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateLoginUserDto {

    @NotBlank(message = "昵称不能为空")
    // @Pattern(regexp = RegexValidConstants.NICK_NAME_REGEX,message = "昵称格式不正确")
    private String nickName;

    @NotBlank(message = "邮箱不能为空")
    @Pattern(regexp = RegexValidConstants.EMAIL_REGEX,message = "请输入正确的邮箱")
    private String email;

    private String phoneNumber;


    @NotBlank(message = "性别不能为空")
    private String sex;

}
