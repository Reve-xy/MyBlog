package cn.ly.framework.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 简要描述
 * 封装返回前端的文章列表
 * @author : Rêve
 * @version : 1.0
 * @date : 2023/4/5 16:42
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleListVo {
    private Long id;
    //标题
    private String title;
    //文章摘要
    private String summary;
    private Long categoryId;

    //所属分类名
    private String categoryName;

    //缩略图
    private String thumbnail;

   /* //是否置顶（0否，1是）
    private String isTop;*/
    //访问量
    private Long viewCount;
    private LocalDateTime createTime;
}
