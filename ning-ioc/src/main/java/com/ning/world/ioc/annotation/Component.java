package com.ning.world.ioc.annotation;

import java.lang.annotation.*;

/**
 * 标志该类是一个组件，会注册到容器当中。
 *
 * @author <a href="guotongning@126.com">Nicholas</a>
 * @since 1.0.0
 * Created on 2021/6/17 21:45
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Component {
    String name();
}
