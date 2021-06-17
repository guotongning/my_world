package com.ning.world.mvc.handler;

import com.ning.world.mvc.enums.ControllerHandleType;
import com.ning.world.mvc.response.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 请求处理
 *
 * @author <a href="guotongning@58.com">Nicholas</a>
 * @since 1.0.0
 */
public interface ControllerHandler {
    /**
     * {@link ControllerHandleType}
     *
     * @return 标识实现类处理的请求类型
     */
    ControllerHandleType handleType();

    Result handle(HttpServletRequest request, HttpServletResponse response, Object controller, Method handleMethod) throws InvocationTargetException, IllegalAccessException;

}
