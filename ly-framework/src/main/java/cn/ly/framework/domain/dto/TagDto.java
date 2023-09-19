package cn.ly.framework.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 简要描述
 *
 * @author : Rêve
 * @version : 1.0
 * @date : 2023/4/15 22:10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagDto {
    private String name;
    private String remark;
    private Long id;

}
