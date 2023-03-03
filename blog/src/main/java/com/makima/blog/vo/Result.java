package com.makima.blog.vo;


import com.makima.blog.constant.StatusCodeEnum;
import lombok.Data;

import static com.makima.blog.constant.StatusCodeEnum.*;

@Data
public class Result<T> {

    private Boolean flag;

    private Integer code;

    private String message;

    private T data;

    public static <T> Result<T> ok(){
        return restResult(true,null, SUCCESS.getCode(),SUCCESS.getDesc());
    }
    public static <T> Result<T> ok(T data) {
        return restResult(true, data, SUCCESS.getCode(), SUCCESS.getDesc());
    }
    public static <T> Result<T> ok(T data, String message) {
        return restResult(true, data, SUCCESS.getCode(), message);
    }
    public static <T> Result<T> fail() {
        return restResult(false, null, FAIL.getCode(), FAIL.getDesc());
    }

    public static <T> Result<T> fail(StatusCodeEnum statusCodeEnum) {
        return restResult(false, null, statusCodeEnum.getCode(), statusCodeEnum.getDesc());
    }
    public static <T> Result<T> fail(String message) {
        return restResult(false, message);
    }

    public static <T> Result<T> fail(T data) {
        return restResult(false, data, FAIL.getCode(), FAIL.getDesc());
    }

    public static <T> Result<T> fail(T data, String message) {
        return restResult(false, data, FAIL.getCode(), message);
    }
    public static <T> Result<T> fail(Integer code, String message) {
        return restResult(false, null, code, message);
    }
    private static <T> Result<T> restResult(Boolean flag, String message) {
        Result<T> apiResult = new Result<>();
        apiResult.setFlag(flag);
        apiResult.setCode(flag ? SUCCESS.getCode() : FAIL.getCode());
        apiResult.setMessage(message);
        return apiResult;
    }

    private static <T> Result<T> restResult(Boolean flag, T data, Integer code, String message) {
        Result<T> apiResult = new Result<>();
        apiResult.setFlag(flag);
        apiResult.setData(data);
        apiResult.setCode(code);
        apiResult.setMessage(message);
        return apiResult;
    }




}
