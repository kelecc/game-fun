package top.kelecc.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import top.kelecc.model.common.dtos.ResponseResult;
import top.kelecc.model.common.enums.HttpCodeEnum;

import java.util.Objects;

/**
 * @author 可乐
 * @version 1.0
 * @description: 全局异常处理
 * @date 2023/7/10 15:38
 */
@ControllerAdvice
@Slf4j
public class ExceptionCatch {
    /**
     * 处理不可控异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseResult exception(Exception e) {
        log.error("catch exception:{}", e.getMessage());
        return ResponseResult.errorResult(HttpCodeEnum.SERVER_ERROR);
    }

    /**
     * 处理可控异常  自定义异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public ResponseResult exception(CustomException e) {
        log.error("catch exception:{}", e);
        return ResponseResult.errorResult(e.getHttpCodeEnum());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseResult exception(MethodArgumentNotValidException e) {
        log.info("非法参数:{}", Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
        return ResponseResult.errorResult(HttpCodeEnum.PARAM_INVALID, Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public ResponseResult exception(HttpMessageNotReadableException e) {
        log.info("RequestBody参数非法:{}", e.getMessage());
        return ResponseResult.errorResult(HttpCodeEnum.PARAM_INVALID, "RequestBody参数非法");
    }
}
