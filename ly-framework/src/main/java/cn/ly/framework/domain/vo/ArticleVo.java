package cn.ly.framework.domain.vo;

import com.baomidou.mybatisplus.annotation.TableId;
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
 * @date : 2023/4/16 11:17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleVo {

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
    private Long createBy;
    private LocalDateTime createTime;
    private Long updateBy;
    private LocalDateTime updateTime;
    //删除标志（0代表未删除，1代表已删除）
    private Integer delFlag;

    private List<Long> tags;
}
