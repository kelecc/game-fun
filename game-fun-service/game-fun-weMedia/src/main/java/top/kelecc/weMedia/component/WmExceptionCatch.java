package top.kelecc.weMedia.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import top.kelecc.model.common.dtos.ResponseResult;
import top.kelecc.model.common.enums.HttpCodeEnum;

/**
 * @author 可乐
 * @version 1.0
 * @description:
 * @date 2023/7/17 17:36
 */
@ControllerAdvice
@Slf4j
public class WmExceptionCatch {
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseResult exception(MaxUploadSizeExceededException e) {
        log.error("catch exception:{}", e.getMessage());
        return ResponseResult.errorResult(HttpCodeEnum.PARAM_INVALID, "上传文件大小不能超过100MB");
    }
}
