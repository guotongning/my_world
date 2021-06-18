package com.ning.world.mvc.handler;

import java.util.Set;

/**
 * 包扫描
 *
 * @author <a href="guotongning@58.com">Nicholas</a>
 * @since 1.0.0
 */
public interface ClassScanner {
    Set<Class<?>> scan(String basePackage);
}
