package com.ning.world.lombok.annotation;

import java.lang.annotation.*;

/**
 * 标记 编译期需要自动生成 Setter方法的类，加在类上实现标记。{@link SetterProcessor}
 *
 * @author <a href="guotongning@58.com">Nicholas</a>
 * @since 1.0.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
@Documented
public @interface Setter {
}
