package com.ning.world.ioc.annotation;

import java.lang.annotation.*;

/**
 * 自动注入注解，添加了该注解的属性值会自动注入
 *
 * @author <a href="guotongning@126.com">Nicholas</a>
 * @since 1.0.0
 * Created on 2021/6/17 21:41
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autowired {
    boolean required() default true;
}
