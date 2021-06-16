package com.ning.world.mvc.handler;


import com.ning.world.mvc.controller.Controller;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * {@link com.ning.world.mvc.servlet.DispatcherServlet}
 * 存储了request mapping和对应method的实体
 *
 * @author <a href="guotongning@58.com">Nicholas</a>
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HttpServletRequestHandler {
    private String requestMapping;
    private Class<?> controllerClazz;
    private Method handleMethod;
    private Set<String> supportedHttpMethods;
}
