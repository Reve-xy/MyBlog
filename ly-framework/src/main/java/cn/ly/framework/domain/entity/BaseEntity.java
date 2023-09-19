package cn.ly.framework.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 简要描述
 * entity共有属性的基类
 * @author : Rêve
 * @version : 1.0
 * @date : 2023/4/24 13:54
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseEntity {
    @TableField(value = "create_by",fill = FieldFill.INSERT)
    private Long createBy;
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(value = "update_by",fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}