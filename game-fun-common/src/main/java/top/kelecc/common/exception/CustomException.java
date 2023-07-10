package top.kelecc.common.exception;

import top.kelecc.model.common.enums.HttpCodeEnum;

/**
 * @author 可乐
 * @version 1.0
 * @description: 自定义异常类
 * @date 2023/7/10 15:06
 */
public class CustomException extends RuntimeException {
    private final HttpCodeEnum httpCodeEnum;

    public CustomException(HttpCodeEnum httpCodeEnum) {
        this.httpCodeEnum = httpCodeEnum;
    }

    public HttpCodeEnum getHttpCodeEnum() {
        return httpCodeEnum;
    }
}
