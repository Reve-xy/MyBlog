package cn.ly.framework.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 简要描述
 *
 * @author : Rêve
 * @version : 1.0
 * @date : 2023/4/27 14:22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryUserDto {
    @NotNull(message = "页码不能为空")
    Integer pageNum;

    @NotNull(message = "每页条数不能为空")
    Integer pageSize;

    // @Pattern(regexp = "^[a-zA-Z0-9_-]{5,19}$",message = "请输入正确的用户名")
    private String userName;

    // @Pattern(regexp = RegexValidConstants.PHONE_REGEX,message = "请输入正确的手机号")
    private String phoneNumber;

    private String status;

    private String type;

}
