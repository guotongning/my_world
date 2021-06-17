package com.ning.world.mvc.response;

import com.ning.world.mvc.enums.ResponseEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * {@link com.ning.world.mvc.controller.RestController} 的返回的数据结构
 *
 * @author <a href="guotongning@58.com">Nicholas</a>
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    /**
     * 如果 view = true,则 message 为页面资源URI
     */
    private boolean view = false;
    private int code;
    private String message;
    private Object data;

    public static Result ok() {
        Result result = new Result();
        result.code = ResponseEnum.OK.getCode();
        result.message = ResponseEnum.OK.getMessage();
        return result;
    }

    public static Result ok(Object data) {
        Result result = new Result();
        result.code = ResponseEnum.OK.getCode();
        result.message = ResponseEnum.OK.getMessage();
        result.data = data;
        return result;
    }

    public static Result view(String view) {
        Result result = new Result();
        result.code = ResponseEnum.OK.getCode();
        result.message = view;
        result.view = true;
        return result;
    }

    public boolean isView() {
        return view;
    }
}
