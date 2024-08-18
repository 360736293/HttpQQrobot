package com.httpqqrobot.result;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel("响应结果")
public class Result implements Serializable {

    @JsonIgnore
    public final long serialVersionUID = 1L;

    @ApiModelProperty(value = "响应代码")
    public int code;

    @ApiModelProperty(value = "响应信息")
    public String msg;

    @ApiModelProperty(value = "响应数据")
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
