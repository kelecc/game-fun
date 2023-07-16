package top.kelecc.user.controller;

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
import top.kelecc.model.user.dto.WmUserLoginDto;
import top.kelecc.security.constants.UserTypeConstans;
import top.kelecc.user.service.LoginService;

import javax.annotation.Resource;

/**
 * @author 可乐
 * @version 1.0
 * @description:
 * @date 2023/7/16 15:37
 */
@RestController
@RequestMapping("/login")
@Api(value = "weMedia登录模块", tags = "weMedia登录模块")
public class WeMediaLoginController {
    @Resource
    private LoginService loginService;

    @PostMapping("/in")
    @ApiOperation("登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "不要带token")
    })
    public ResponseResult login(@RequestBody WmUserLoginDto user) {
        if (StringUtils.isNotBlank(user.getPassword()) && StringUtils.isNotBlank(user.getName())) {
            return loginService.login(user.getName(), user.getPassword(), UserTypeConstans.WE_MEDIA_USER);
        }
        return ResponseResult.errorResult(HttpCodeEnum.PARAM_INVALID, "用户名或者密码不能为空！");
    }

    @ApiOperation("登出")
    @PostMapping("/out")
    public Object logout() {
        return loginService.logout(UserTypeConstans.WE_MEDIA_USER);
    }
}
