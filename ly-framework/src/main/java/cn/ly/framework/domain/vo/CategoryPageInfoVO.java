package cn.ly.framework.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 简要描述
 *
 * @author : Rêve
 * @version : 1.0
 * @date : 2023/6/8 23:23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryPageInfoVO {
    private Long id;
    private String name;
    private String description;
    private String status;
    private Long articleCount;
}
