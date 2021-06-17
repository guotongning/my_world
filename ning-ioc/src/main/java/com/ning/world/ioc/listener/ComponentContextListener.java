package com.ning.world.ioc.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * {@link com.ning.world.ioc.context.NingComponentContext}初始化组件容器上下文
 *
 * @author <a href="guotongning@126.com">Nicholas</a>
 * @since 1.0.0
 * Created on 2021/6/17 21:52
 */
@WebListener
public class ComponentContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
