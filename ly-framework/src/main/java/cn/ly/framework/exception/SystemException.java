package cn.ly.framework.exception;

import cn.ly.framework.enums.HttpCodeEnum;

/**
 * 简要描述
 * 自定义异常处理
 * @author : Rêve
 * @version : 1.0
 * @date : 2023/4/7 9:57
 */
public class SystemException extends RuntimeException{

    private Integer code;

    private String msg;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public SystemException(HttpCodeEnum httpCodeEnum) {
        super(httpCodeEnum.getMsg());
        this.code = httpCodeEnum.getCode();
        this.msg = httpCodeEnum.getMsg();
    }
    public SystemException(Integer code,String msg) {
        super(msg);
        this.code =code;
        this.msg =msg;
    }
}
