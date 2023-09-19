package cn.ly.framework.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 简要描述
 *
 * @author : Rêve
 * @version : 1.0
 * @date : 2023/4/27 17:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDto{
    @NotNull(message = "id不能为空")
    private Long id;

    @NotBlank(message = "用户名不能为空")
    // @Pattern(regexp = "^[a-zA-Z0-9_-]{5,19}$",message = "请输入正确的用户名")
    private String userName;

    @NotBlank(message = "状态不能为空")
    private String status;

    @NotBlank(message = "电话号码不能为空")
    private String phoneNumber;

    /**
     * @description 性别选填
     */
    private String sex;


    @NotBlank(message = "昵称不能为空")
    private String nickName;

    @NotBlank(message = "邮箱不能为空")
    // @Pattern(regexp = RegexValidConstants.EMAIL_REGEX,message = "请输入正确的邮箱")
    private String email;

    private Long[] roleIds;
}
