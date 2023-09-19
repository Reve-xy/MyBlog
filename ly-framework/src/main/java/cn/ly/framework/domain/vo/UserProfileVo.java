package cn.ly.framework.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 简要描述
 *
 * @author : Rêve
 * @version : 1.0
 * @date : 2023/5/6 23:38
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserProfileVo {
    private Long id;
    private String nickName;
    private String userName;
    private String email;
    private String phoneNumber;
    private String sex;
    private List<String > roleGroups;
    private LocalDateTime createTime;
}
