package com.ning.world.mvc.annotation;

import java.lang.annotation.*;

/**
 * 定义请求路径
 *
 * @author <a href="guotongning@58.com">Nicholas</a>
 * @since 1.0.0
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {
    String value();
}
