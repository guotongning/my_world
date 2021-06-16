package com.ning.world.mvc.response;

import javax.servlet.http.HttpServletResponse;

/**
 * {@link com.ning.world.mvc.controller.RestController} 的返回的数据结构
 *
 * @author <a href="guotongning@58.com">Nicholas</a>
 * @since 1.0.0
 */
public class Result {
    /**
     * 如果 view = true,则 message 为页面资源URI
     */
    private boolean view = false;
    private int code;
    private String message;
    private Object data;

    public Result(Object data) {
        this.code = HttpServletResponse.SC_OK;
        this.message = "success";
        this.data = data;
    }

    public Result(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Result(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
