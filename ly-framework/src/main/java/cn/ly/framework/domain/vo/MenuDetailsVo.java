package cn.ly.framework.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 简要描述
 * 菜单基本信息回显修改vo
 * @author : Rêve
 * @version : 1.0
 * @date : 2023/4/24 20:24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuDetailsVo {
    private Long id;
    private String menuName;
    private Long parentId;
    private Integer orderNum;
    private String path;
    private String menuType;
    private String visible;
    private String status;
    private String icon;
    private String remark;

}
