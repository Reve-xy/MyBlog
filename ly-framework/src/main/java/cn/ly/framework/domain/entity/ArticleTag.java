package cn.ly.framework.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import lombok.*;

/**
 * <p>
 * 文章标签关联表
 * </p>
 *
 * @author 刘易
 * @since 2023-04-16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("ly_article_tag")
public class ArticleTag implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文章id
     */
    @TableId(value = "article_id", type = IdType.INPUT)
    private Long articleId;

    /**
     * 标签id
     */
    private Long tagId;


}
