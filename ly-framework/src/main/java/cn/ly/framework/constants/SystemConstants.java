package cn.ly.framework.constants;

/**
 * 简要描述
 *
 * @author : Rêve
 * @version : 1.0
 * @date : 2023/4/3 14:48
 */
public class SystemConstants {

    /**
     *  文章是草稿
     */
    public static final String ARTICLE_STATUS_DRAFT = "1";
    /**
     *  文章是正常发布状态
     */
    public static final String ARTICLE_STATUS_NORMAL = "0";
    /**
     * 友链状态为审核通过
     */
    public static final  String LINK_STATUS_NORMAL = "0";

    public static final String CATEGORY_STATUS_NORMAL = "0";

    /**
    * 评论列表的根评论
    * */
    public static final Long COMMENT_ROOT_ID=-1L;

    /**
     * 评论类型为：文章评论
     */
    public static final String ARTICLE_COMMENT = "0";
    /**
     * 不允许评论
     */
    public static final String EXIT_COMMENT = "1";
    /**
     * 评论类型为：友联评论
     */
    public static final String LINK_COMMENT = "1";


    /**
     * 菜单类型为菜单
     */
    public static final String MENU="C";

    /**
     * 菜单类型为按钮
     */
    public static final String BUTTON="F";

    /**
     * 菜单状态为正常
     */
    public static final String STATUS_NORMAL="0";
    /** 正常状态 */
    public static final String NORMAL = "0";

    /**
     * 超管id
     */
    public static final Long ADMIN_ID = 1L;

    public static final String ADMIN_TYPE="1";
    public static final String NORMAL_TYPE="0";


    public static final String UPDATE = "UPDATE";
    public static final String INSERT = "INSERT";
    /**
     * 默认性别，未知--->2
     */
    public static final String DEFAULT_SEX="2";
    public static final Long MENU_ROOT_ID=0L;

    public static final Long NORMAL_ROLE=2L;
}
