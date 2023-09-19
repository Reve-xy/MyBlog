package cn.ly.framework.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 简要描述
 *
 * @author : Rêve
 * @version : 1.0
 * @date : 2023/7/17 11:21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IndexDataVo {
    Integer userCount;
    Integer articleCount;
    Integer categoryCount;
    Integer tagCount;
    Integer viewCount;
}
