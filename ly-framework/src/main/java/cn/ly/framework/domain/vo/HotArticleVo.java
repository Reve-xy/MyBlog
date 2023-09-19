package cn.ly.framework.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 简要描述
 * 热门文章DTO
 * @author : Rêve
 * @version : 1.0
 * @date : 2023/4/3 14:43
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotArticleVo {

    private Long id;

    //标题
    private String title;

    //访问量
    private Long viewCount;
}
