package top.kelecc.security.component;

import com.alibaba.fastjson.JSON;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import top.kelecc.model.common.dtos.ResponseResult;
import top.kelecc.model.common.enums.HttpCodeEnum;
import top.kelecc.security.utils.WebUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 可乐
 * @version 1.0
 * @description:
 * @date 2023/5/17 22:52
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    /**
     * @param httpServletRequest
     * @param httpServletResponse
     * @param e
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        Object result = ResponseResult.errorResult(HttpCodeEnum.AUTH_FAILED);
        String jsonString = JSON.toJSONString(result);
        WebUtils.renderString(httpServletResponse, jsonString);

    }
}
