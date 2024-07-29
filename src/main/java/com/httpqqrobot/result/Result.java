package com.httpqqrobot.result;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

public class Result implements Serializable {

    @JsonIgnore
    public final long serialVersionUID = 1L;

    public int code;

    public String msg;

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
