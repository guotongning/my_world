package com.ning.world.mvc.enums;

/**
 * TODO
 *
 * @author <a href="guotongning@58.com">Nicholas</a>
 * @since
 */
public enum ResponseEnum {
    OK(200, "OK"),
    ERROR(500, "ERROR"),
    FAIL(1001, "FAIL"),
    ;
    private int code;
    private String message;

    ResponseEnum(int code, String message) {
        this.code = code;
        this.message = message;
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

    public ResponseEnum code2Enum(int code) {
        for (ResponseEnum value : values()) {
            if (value.code == code) {
                return value;
            }
        }
        return FAIL;
    }

}
