package cn.ly.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 简要描述
 * 自定义注解，操作aop
 * @author : Rêve
 * @version : 1.0
 * @date : 2023/4/12 22:22
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SystemLog {
    String value();
}
