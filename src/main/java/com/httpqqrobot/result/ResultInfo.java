package com.httpqqrobot.result;

public enum ResultInfo {
    SUCCESS(200, "成功"),
    UNKNOWFAIL(-1, "未知失败"),
    ERROR(500, "服务器异常"),
    SERVICEUNAVAILABLE(503, "服务不可用"),
    UNAUTHORIZED(401, "未认证"),
    FORBIDDEN(403, "禁止访问");

    private int code;

    private String msg;

    ResultInfo(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

}
