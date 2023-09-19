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
 * @date : 2023/4/6 10:12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDetailVo {

    private Long categoryId;

    private String categoryName;

    private String content;

    private LocalDateTime createTime;

    private Long id;

    //是否允许评论 1是，0否
    private String isComment;

    //标题
    private String title;

    //访问量
    private Long viewCount;

    private List<String> tagNames;

}
