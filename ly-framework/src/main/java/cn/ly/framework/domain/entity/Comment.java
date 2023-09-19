package cn.ly.framework.domain.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.*;

/**
 * <p>
 * 评论表
 * </p>
 *
 * @author 刘易
 * @since 2023-04-09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("ly_comment")
public class Comment extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 评论类型（0代表文章评论，1代表友链评论）
     */
    @TableField("type")
    private String type;

    /**
     * 文章id
     */
    @TableField("article_id")
    private Long articleId;

    /**
     * 根评论id
     */
    @TableField("root_id")
    private Long rootId;

    /**
     * 评论内容
     */
    @TableField("content")
    private String content;

    /**
     * 所回复的目标评论的userid
     */
    @TableField("to_comment_user_id")
    private Long toCommentUserId;

    /**
     * 回复目标评论id
     */
    @TableField("to_comment_id")
    private Long toCommentId;

    /**
     * 删除标志（0代表未删除，1代表已删除）
     */
    @TableField("del_flag")
    private Integer delFlag;


}
