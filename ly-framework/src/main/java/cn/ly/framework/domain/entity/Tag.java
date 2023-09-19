package cn.ly.framework.domain.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.*;

/**
 * <p>
 * 标签
 * </p>
 *
 * @author 刘易
 * @since 2023-04-14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("ly_tag")
public class Tag extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 标签名
     */
    @TableField("name")
    private String name;


    /**
     * 删除标志（0代表未删除，1代表已删除）
     */
    @TableField("del_flag")
    private Integer delFlag;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;


}
