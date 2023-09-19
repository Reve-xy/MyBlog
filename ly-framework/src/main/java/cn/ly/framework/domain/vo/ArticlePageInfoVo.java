package cn.ly.framework.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 简要描述
 *
 * @author : Rêve
 * @version : 1.0
 * @date : 2023/6/8 21:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticlePageInfoVo {
    private Long id;

    //标题
    private String title;

    //文章内容
    private String content;

    //文章摘要
    private String summary;

    //所属分类id
    private Long categoryId;


    //缩略图
    private String thumbnail;

    //是否置顶（0否，1是）
    private String isTop;

    //状态（0已发布，1草稿）
    private String status;

    //访问量
    private Long viewCount;

    //是否允许评论 1是，0否
    private String isComment;
    private LocalDateTime createTime;

}