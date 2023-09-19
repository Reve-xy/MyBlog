package cn.ly.framework.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 简要描述
 * 路由vo
 * @author : Rêve
 * @version : 1.0
 * @date : 2023/4/24 14:13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class MenuRouterVo {
    private Long id;
    private String menuName;
    private Long parentId;
    private Integer orderNum;
    private String path;
    private String component;
    private String menuType;
    private String visible;
    private String status;
    private String perms;
    private String icon;
    List<MenuRouterVo> children;
}
