package com.makima.blog.exception;



import com.makima.blog.constant.StatusCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.makima.blog.constant.StatusCodeEnum.FAIL;


/**
 * 业务异常
 *
 * @author yezhiqiu
 * @date 2021/07/27
 */
@Getter
@AllArgsConstructor
public class BizException extends RuntimeException {

    /**
     * 错误码
     */
    private Integer code = FAIL.getCode();

    /**
     * 错误信息
     */
    private final String message;

    public BizException(String message) {
        this.message = message;
    }

    public BizException(StatusCodeEnum statusCodeEnum) {
        this.code = statusCodeEnum.getCode();
        this.message = statusCodeEnum.getDesc();
    }


}
