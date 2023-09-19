package cn.ly.framework.domain.entity;

import java.util.Date;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.experimental.Accessors;

/**
 * 文章表(Article)表实体类
 * @Accessors--->链式编程
 * @author Rêve
 * @since 2023-04-02 21:26:32
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("ly_article")
public class Article extends BaseEntity implements Serializable{

    private static final long serialVersionUID = 1L;

    public Article(Long id, Long viewCount) {
        this.id = id;
        this.viewCount = viewCount;
    }

    @TableId
    private Long id;
        
    //标题    
    private String title;
        
    //文章内容    
    private String content;
        
    //文章摘要    
    private String summary;
        
    //所属分类id    
    private Long categoryId;

    @TableField(exist = false)
    private String categoryName;
        
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

    //删除标志（0代表未删除，1代表已删除）    
    private Integer delFlag;

}
