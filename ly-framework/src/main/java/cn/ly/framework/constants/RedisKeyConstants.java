package cn.ly.framework.constants;

/**
 * 简要描述
 * redis的键
 *
 * @author : Rêve
 * @version : 1.0
 * @date : 2023/4/6 15:35
 */
public class RedisKeyConstants {


    /**
     * 验证码 redis key
     */
    public static final String CAPTCHA_KEY = "captcha:code:";

    /**
     * email captcha redis key
     */
    public static final String EMAIL_CAPTCHA_KEY = "email:captcha:code:";

    /**
     * resetPwd email redis key
     */
    public static final String RESET_EMAIL_KEY = "reset:email:key";

    //USER

    /**
     * 用户信息
     */
    public static final String USER_INFO_KEY="user:info:";


    /*
     * 博客登录key
     * */
    public static final String BLOG_LOGIN_KEY="BLOG:LOGIN:";

    /*
     * 博客登录key
     * */
    public static final String ADMIN_LOGIN_KEY="ADMIN:LOGIN:";

    /*
     * 浏览量存储
     * */
    public static final String ARTICLE_VIEW_COUNT="article:viewCount";



    /*----------------------------------后台--------------------------------------------*/

    //用户菜单管理
    public static final String MENU="menu";

    /**
     * 菜单列表
     */
    public static final String MENU_LIST_KEY = "menu:list:";


    public static final String ROLE="role";

    /**
     * 用户路由
     */
    public static final String ROLE_MENU_ROUTES = "role:menu:routes:";

    /**
     * 用户权限
     */
    public static final String ROLE_MENU_PERMS = "role:menu:perms";

    /**
     * 角色已激活的菜单
     */
    public static final String ROLE_ACT_MENU = "role:act:menu:";

    /**
     * 角色key
     */
    public static final String ROLE_KEY = "role:key:";
    /**
     * 角色id
     */
    public static final String ROLE_ID_KEY="role:id:";
    /**
     * 角色列表
     */
    public static final String ROLE_LIST_KEY="role:list:";
    /**
     * 所有正常的角色
     */
    public static final String ROLE_STATUS_KEY= "role:status:normal:";

    public static final String USER="user";

    /**
     * 用户列表
     */
    public static final String USER_LIST_KEY="user:list:";

    //用户文章管理
    //文章
    public static final String ARTICLE_INFO_KEY="article:info:";
    public static final String ARTICLE_DETAIL_KEY="article:detail:";

    //友情链接
    public static final String LINK_LIST_KEY="link:list";
    //分类
    public static final String CATEGORY_LIST_KEY="category:list";
    //标签
    public static final String TAG_LIST_KEY="tag:list";
}
