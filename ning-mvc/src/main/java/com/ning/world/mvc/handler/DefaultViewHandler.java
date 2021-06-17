package com.ning.world.mvc.handler;

import com.ning.world.mvc.controller.Controller;
import com.ning.world.mvc.enums.ControllerHandleType;
import com.ning.world.mvc.response.Result;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * {@link ControllerHandler}默认的处理 view请求的处理器
 *
 * @author <a href="guotongning@58.com">Nicholas</a>
 * @since 1.0.0
 */
public class DefaultViewHandler implements ControllerHandler {

    @Override
    public ControllerHandleType handleType() {
        return ControllerHandleType.VIEW;
    }

    @Override
    public Result handle(HttpServletRequest request, HttpServletResponse response, Object controller, Method handleMethod) {
        try {
            Result result = invokeHandleMethod(controller, handleMethod);
            ServletContext servletContext = request.getServletContext();
            String resource = handleRequestMapping(result.getMessage() + ".html");
            RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(resource);
            response.setHeader("ForwardMarker", "1");
            requestDispatcher.forward(request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private Result invokeHandleMethod(Object controller, Method handleMethod) throws IllegalAccessException, InvocationTargetException {
        return Result.view((String) handleMethod.invoke(controller));
    }

    public static String handleRequestMapping(String requestMapping) {
        if (requestMapping == null || "".equals(requestMapping)) {
            return "";
        }
        if (!requestMapping.startsWith("/")) {
            return "/" + requestMapping;
        }
        return requestMapping;
    }
}
