package com.ning.world.mvc.servlet;

import com.alibaba.fastjson.JSON;
import com.ning.world.mvc.annotation.RequestMapping;
import com.ning.world.mvc.annotation.RestController;
import com.ning.world.mvc.annotation.ViewController;
import com.ning.world.mvc.enums.ControllerHandleType;
import com.ning.world.mvc.handler.ClassScanner;
import com.ning.world.mvc.handler.ControllerHandler;
import com.ning.world.mvc.handler.HttpServletRequestHandler;
import com.ning.world.mvc.response.Result;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

/**
 * {@link javax.servlet.Servlet} - Controller Request和Handler的映射
 *
 * @author <a href="guotongning@58.com">Nicholas</a>
 * @since 1.0.0
 */
public class DispatcherServlet extends HttpServlet {
    private ServletContext servletContext;
    private ControllerHandler restControllerHandler;
    private ControllerHandler viewControllerHandler;
    private ClassScanner classScanner;
    private final Map<String, HttpServletRequestHandler> pathHandleMapping = new LinkedHashMap<>();
    private final Set<String> requestMappings = new HashSet<>();
    private static final Set<String> DEFAULT_SUPPORTED_HTTP_METHODS = new LinkedHashSet<>(asList(HttpMethod.GET, HttpMethod.POST,
            HttpMethod.PUT, HttpMethod.DELETE, HttpMethod.HEAD, HttpMethod.OPTIONS));

    public void init() {
        servletContext = this.getServletContext();
        servletContext.log("DispatcherServlet init start");
        initClassScanner();
        initControllerHandler();
        initPathHandleMapping();
    }

    private void initControllerHandler() {
        servletContext.log("DispatcherServlet ControllerHandler init start");
        ServiceLoader<ControllerHandler> controllerHandlers = ServiceLoader.load(ControllerHandler.class);
        for (ControllerHandler controllerHandler : controllerHandlers) {
            ControllerHandleType handleType = controllerHandler.handleType();
            switch (handleType) {
                case REST:
                    if (this.restControllerHandler == null) {
                        this.restControllerHandler = controllerHandler;
                    }
                    break;
                case VIEW:
                    if (this.viewControllerHandler == null) {
                        this.viewControllerHandler = controllerHandler;
                    }
                    break;
                default:
                    throw new RuntimeException("Must specify the type of request handled by the implementation class！@see com.ning.world.mvc.enums.ControllerHandleType");
            }
        }
    }

    private void initClassScanner() {
        servletContext.log("DispatcherServlet ClassScanner init start");
        ServiceLoader<ClassScanner> classScanners = ServiceLoader.load(ClassScanner.class);
        for (ClassScanner classScanner : classScanners) {
            if (this.classScanner == null) {
                this.classScanner = classScanner;
            }
            break;
        }
    }

    private void initPathHandleMapping() {
        servletContext.log("DispatcherServlet PathHandleMapping init start");
        classScanner.scan("com.ning").stream()
                .filter(clazz -> {
                    RestController restController = clazz.getAnnotation(RestController.class);
                    ViewController viewController = clazz.getAnnotation(ViewController.class);
                    return restController != null || viewController != null;
                })
                .map(this::initRequestHandlerMap)
                .forEach(pathHandleMapping::putAll);
    }

    private Map<String, HttpServletRequestHandler> initRequestHandlerMap(Class<?> clazz) {
        BeanInfo beanInfo;
        Map<String, HttpServletRequestHandler> requestHandlerMap = new LinkedHashMap<>();
        try {
            beanInfo = Introspector.getBeanInfo(clazz);
            Annotation[] annotationOnClass = beanInfo != null ? clazz.getAnnotations() : new Annotation[0];
            for (Annotation annotation : annotationOnClass) {
                if (!(annotation instanceof Path) && !(annotation instanceof RequestMapping)) {
                    continue;
                }
                String preMapping = getPreMapping(annotation);
                Collection<MethodDescriptor> methodDescriptors = getRequestHandleMethods(beanInfo);
                for (MethodDescriptor methodDescriptor : methodDescriptors) {
                    Method method = methodDescriptor.getMethod();
                    Annotation[] annotations = method.getAnnotations();
                    String mappingPath = handleMappingPath(preMapping) + handleMappingPath(getAfterMapping(annotations));
                    if (!requestMappings.add(mappingPath)) {
                        throw new RuntimeException("caveat! There are duplicate request path definitions!");
                    }
                    Set<String> supportedHttpMethods = getSupportedHttpMethods(annotations);
                    ControllerHandleType handleType = clazz.getAnnotation(RestController.class) == null ? ControllerHandleType.VIEW : ControllerHandleType.REST;
                    requestHandlerMap.put(mappingPath, new HttpServletRequestHandler(mappingPath, handleType, clazz.newInstance(), method, supportedHttpMethods));
                }
            }
        } catch (Exception e) {
            servletContext.log("create HttpServletRequestHandler error!", e);
        }
        return requestHandlerMap;
    }

    private Collection<MethodDescriptor> getRequestHandleMethods(BeanInfo beanInfo) {
        return Arrays.stream(beanInfo.getMethodDescriptors()).filter(methodDescriptor -> {
            Method method = methodDescriptor.getMethod();
            int mod = method.getModifiers();
            RequestMapping methodAnnotation = method.getAnnotation(RequestMapping.class);
            return Modifier.isPublic(mod) && methodAnnotation != null;
        }).collect(Collectors.toList());
    }

    private String handleMappingPath(String mappingPath) {
        if (StringUtils.isEmpty(mappingPath)) {
            return "";
        }
        if (!mappingPath.startsWith("/")) {
            mappingPath = "/" + mappingPath;
        }
        if (mappingPath.endsWith("/")) {
            mappingPath = mappingPath.substring(0, mappingPath.length() - 1);
        }
        return mappingPath;
    }

    private Set<String> getSupportedHttpMethods(Annotation[] annotations) {
        Set<String> supportedHttpMethods = new LinkedHashSet<>();
        for (Annotation annotation : annotations) {
            if (annotation instanceof GET) {
                supportedHttpMethods.add(HttpMethod.GET);
            } else if (annotation instanceof POST) {
                supportedHttpMethods.add(HttpMethod.POST);
            }
        }
        if (supportedHttpMethods.size() == 0) {
            return DEFAULT_SUPPORTED_HTTP_METHODS;
        }
        return supportedHttpMethods;
    }

    private String getAfterMapping(Annotation[] annotations) {
        String postMapping = "";
        for (Annotation annotationOnMethod : annotations) {
            if (annotationOnMethod instanceof Path) {
                postMapping = ((Path) annotationOnMethod).value();
            } else if (annotationOnMethod instanceof RequestMapping) {
                postMapping = ((RequestMapping) annotationOnMethod).value();
            }
        }
        return postMapping;
    }

    private String getPreMapping(Annotation annotation) {
        String preMapping = "";
        if (annotation instanceof Path) {
            preMapping = ((Path) annotation).value();
        } else if (annotation instanceof RequestMapping) {
            preMapping = ((RequestMapping) annotation).value();
        }
        return preMapping;
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) {
        String requestURI = request.getRequestURI();
        HttpServletRequestHandler httpServletRequestHandler = pathHandleMapping.get(requestURI);
        if (httpServletRequestHandler == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        if (!httpServletRequestHandler.getSupportedHttpMethods().contains(request.getMethod())) {
            throw new RuntimeException("request method is not supported！");
        }
        ControllerHandleType handleType = httpServletRequestHandler.getHandleType();
        Object controller = httpServletRequestHandler.getController();
        Method handleMethod = httpServletRequestHandler.getHandleMethod();
        try {
            switch (handleType) {
                case REST:
                    Result result = restControllerHandler.handle(request, response, controller, handleMethod);
                    response.getWriter().write(JSON.toJSONString(result));
                    break;
                case VIEW:
                    viewControllerHandler.handle(request, response, controller, handleMethod);
                    break;
                default:
                    throw new RuntimeException("request could not be processed！");
            }
        } catch (Exception e) {
            servletContext.log("service error!", e);
        }

    }
}
