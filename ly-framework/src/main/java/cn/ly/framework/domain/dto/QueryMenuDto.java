package cn.ly.framework.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 简要描述
 *
 * @author : Rêve
 * @version : 1.0
 * @date : 2023/4/24 20:07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryMenuDto {
    private String status;
    private String menuName;
}
