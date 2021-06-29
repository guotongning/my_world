package com.ning.world.lombok.annotation;

import com.ning.world.lombok.processor.GetterProcessor;

import java.lang.annotation.*;

/**
 * 标记 编译期需要自动生成 Getter方法的类，加在类上实现标记。{@link GetterProcessor}
 *
 * @author <a href="guotongning@58.com">Nicholas</a>
 * @since 1.0.0
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface Getter {

}
