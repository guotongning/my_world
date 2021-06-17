package com.ning.world.ioc.context;

import javax.servlet.ServletContext;

/**
 * 组件容器上下文
 *
 * @author <a href="guotongning@126.com">Nicholas</a>
 * @since 1.0.0
 * Created on 2021/6/17 21:50
 */
public class NingComponentContext {
    public static final String CONTEXT_NAME = "COMPONENT_CONTEXT";
    private static ServletContext servletContext;
}
