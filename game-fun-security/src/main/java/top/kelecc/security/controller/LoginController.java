package top.kelecc.security.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.kelecc.security.service.LoginService;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author 可乐
 * @version 1.0
 * @description:
 * @date 2023/5/17 15:01
 */
@RestController
@RequestMapping("/api/v1")
public class LoginController {
    @Resource
    private LoginService loginService;

    @PostMapping("/login")
    public Object login(@RequestBody Map<String, String> map) {
        return loginService.login(map.get("phone"), map.get("password"));
    }

    @PostMapping("/logout")
    public Object logout() {
        return loginService.logout();
    }
}
