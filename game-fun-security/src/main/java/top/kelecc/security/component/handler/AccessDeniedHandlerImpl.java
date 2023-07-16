package top.kelecc.security.component.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import top.kelecc.model.common.dtos.ResponseResult;
import top.kelecc.model.common.enums.HttpCodeEnum;

/**
 * @author 可乐
 * @version 1.0
 * @description:
 * @date 2023/5/17 22:49
 */
@ControllerAdvice
@Slf4j
public class AccessDeniedHandlerImpl {
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public ResponseResult exception(AccessDeniedException e) {
        log.info("access denied: {}", e.getMessage());
        return ResponseResult.errorResult(HttpCodeEnum.ACCESS_DENIED);
    }
}
