package cn.ly.framework.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 分类表
 * </p>
 *
 * @author 刘易
 * @since 2023-04-05
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("ly_category")
public class Category extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 分类名
     */
    @TableField("name")
    private String name;

    /**
     * 父分类id，如果没有父分类为-1
     */
    @TableField("pid")
    private Long pid;

    /**
     * 描述
     */
    @TableField("description")
    private String description;

    /**
     * 状态0:正常,1禁用
     */
    @TableField("status")
    private String status;

    /**
     * 删除标志（0代表未删除，1代表已删除）
     */
    @TableField("del_flag")
    private Integer delFlag;

    @TableField(exist = false)
    private Long articleCount;
}
