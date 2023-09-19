package cn.ly.framework.enums;

public enum HttpCodeEnum {
    // 成功
    // 成功
    SUCCESS(200,"操作成功"),
    SUCCESS_LOGOUT(200,"退出成功"),
    SUCCESS_REGISTER(200,"注册成功"),
    SUCCESS_LOGIN(200,"登录成功"),
    /*6系均为表单回显*/
    // 登录
    LOGIN_ERROR(61,"用户名或密码错误"),
    REQUIRE_ACCOUNT(61,"必须填写账号"),
    USER_NOT_FOUND(61,"用户不存在"),
    NOT_ADMIN(61,"只有管理员账号才能登录"),


    //注册
    EMAIL_EXIST(66, "邮箱已被注册"),
    USERNAME_EXIST(67,"用户名已存在,换个试试吧"),



    CAPTCHA_ERR(62,"验证码错误"),
    CAPTCHA_EXPIRE(62,"验证码已过期"),
    CERTIFICATE_EXPIRE(88,"凭证已过期，请重新验证"),   //忘记密码凭证过期
    USER_DISABLED(77,"您的账号存在风险暂时无法使用"),
    NEED_LOGIN(401,"需要登录后操作"),
    NO_OPERATOR_AUTH(403,"无权限操作"),
    SYSTEM_ERROR(500,"服务器错误"),

    USERNAME_NOT_NULL(508, "用户名不能为空"),
    NICKNAME_NOT_NULL(509, "昵称不能为空"),
    EMAIL_NOT_NULL(511, "邮箱不能为空"),
    NICKNAME_EXIST(512, "昵称已存在"),
    CAPTCHA_NULL(512,"验证码不能为空"),
    UUID_NULL(514,"客户端异常"),

    REPEAT_USERNAME(515,"该账号名已存在了，换个试试吧"),

    REPEAT_EMAIL(516,"该邮箱已注册过了"),

    PHONE_EXIST(517,"该手机号已注册过了"),
    PWD_NOT_SAME(518,"两次密码不一致"),

    SEX_ERR(519,"性别选择异常"),

    HAS_CHILD_MENU(520,"存在子菜单，无法删除"),
    ILLEGAL_ID(523,"请输入正确的用户名"),
    ILLEGAL_EMAIL(523,"请输入正确的邮箱"),

    FAILURE_UPLOAD(523,"文件上传失败"),
    NO_EXIST_EMAIL(524,"该邮箱还没有注册过哦"),
    CONTENT_NOT_NULL(525,"内容不能为空" ),
    EXIT_COMMENT(525,"作者关闭了此文章的评论功能" ),
    IMG_TYPE_ERROR(526,"文件类型错误，请上传jpg或png格式的图片" ),

    ARTICLE_ERROR(527,"文章被作者隐藏或已被删除");


    int code;
    String msg;

    HttpCodeEnum(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
