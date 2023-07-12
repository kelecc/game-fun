package top.kelecc.test.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.kelecc.model.common.dtos.ResponseResult;

import java.util.HashMap;

/**
 * @author 可乐
 * @version 1.0
 * @description:
 * @date 2023/7/12 13:28
 */
@RestController
@RequestMapping("/api/v1/test")
public class TestController {
    @GetMapping("/hello")
    public ResponseResult hello() {
        HashMap<String, String> map = new HashMap<>();
        map.put("name", "kele");
        return ResponseResult.okResult(map);
    }

    @GetMapping("/hello2")
    @PreAuthorize("hasAuthority('kele')")
    public ResponseResult hello2() {
        HashMap<String, String> map = new HashMap<>();
        map.put("name", "kele");
        return ResponseResult.okResult(map);
    }
}
