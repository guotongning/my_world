package com.ning.world.mvc.handler;

import com.ning.world.mvc.enums.ControllerHandleType;
import com.ning.world.mvc.response.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * {@link ControllerHandler}默认的处理 rest请求的处理器
 *
 * @author <a href="guotongning@58.com">Nicholas</a>
 * @since 1.0.0
 */
public class DefaultRestHandler implements ControllerHandler {

    @Override
    public ControllerHandleType handleType() {
        return ControllerHandleType.REST;
    }

    @Override
    public Result handle(HttpServletRequest request, HttpServletResponse response, Object controller, Method handleMethod) throws InvocationTargetException, IllegalAccessException {
        return (Result) handleMethod.invoke(controller);
    }


}
