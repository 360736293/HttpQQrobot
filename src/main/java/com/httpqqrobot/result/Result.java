package com.httpqqrobot.result;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

public class Result implements Serializable {

    @JsonIgnore
    public final long serialVersionUID = 1L;

    /**
     * 响应代码
     */
    public int code;

    /**
     * 响应信息
     */
    public String msg;

    /**
     * 响应数据
     */
    public Object data;

    Result(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static Result success(int code, String msg, Object data) {
        return new Result(code, msg, data);
    }

    public static Result fail(int code, String msg, Object data) {
        return new Result(code, msg, data);
    }
}
