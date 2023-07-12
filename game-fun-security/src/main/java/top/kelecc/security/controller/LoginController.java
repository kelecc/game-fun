package top.kelecc.security.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.kelecc.model.common.dtos.ResponseResult;
import top.kelecc.model.common.enums.HttpCodeEnum;
import top.kelecc.model.common.user.dto.UserLoginDto;
import top.kelecc.security.service.LoginService;

import javax.annotation.Resource;

/**
 * @author 可乐
 * @version 1.0
 * @description:
 * @date 2023/5/17 15:01
 */
@RestController
@RequestMapping("/api/v1")
@Api(value = "登录模块",tags = "登录模块")
public class LoginController {
    @Resource
    private LoginService loginService;

    @PostMapping("/login")
    @ApiOperation("登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token",value = "不要带token")
    })
    public Object login(@RequestBody UserLoginDto user) {
        if (StringUtils.isNotBlank(user.getPassword()) && StringUtils.isNotBlank(user.getPhone())) {
            return loginService.login(user.getPhone(), user.getPassword());
        }
        return ResponseResult.errorResult(HttpCodeEnum.PARAM_INVALID, "手机号或者密码不能为空！");
    }
    @ApiOperation("登出")
    @PostMapping("/logout")
    public Object logout() {
        return loginService.logout();
    }
}
