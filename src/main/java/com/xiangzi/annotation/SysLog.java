package com.xiangzi.annotation;

import java.lang.annotation.*;

/**
 * description
 * author:张翔翔
 * Date:2019/5/21
 * Time:15:21
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {

    String value() default "";
}
