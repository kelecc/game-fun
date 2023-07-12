package top.kelecc.security.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import top.kelecc.model.common.dtos.ResponseResult;
import top.kelecc.model.common.enums.HttpCodeEnum;
import top.kelecc.security.component.DaoUserDetails;
import top.kelecc.security.dao.UserMapper;
import top.kelecc.security.service.LoginService;
import top.kelecc.security.utils.RedisCache;
import top.kelecc.utils.common.JwtUtil;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * @author 可乐
 * @version 1.0
 * @description:
 * @date 2023/5/17 15:06
 */
@Service
public class LoginServiceImpl implements LoginService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private RedisCache redisCache;
    @Resource
    private AuthenticationManager authenticationManager;

    /**
     * 登录
     *
     * @param username
     * @param password
     * @return
     */
    @Override
    public Object login(String username, String password) {
        //认证
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authenticate = null;
        try {
            authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        } catch (AuthenticationException e) {
            return ResponseResult.errorResult(3, e.getMessage());
        }
        DaoUserDetails userDetails = (DaoUserDetails) authenticate.getPrincipal();
        //生成jwt
        String userId = userDetails.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);
        //将authentication存入redis
        redisCache.setCacheObject("login:" + userId, userDetails);
        //返回token
        HashMap<String, String> map = new HashMap<>();
        map.put("token", jwt);
        return ResponseResult.okResult(map);
    }

    /**
     * @return
     */
    @Override
    public Object logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        DaoUserDetails daoUserDetails = (DaoUserDetails) authentication.getPrincipal();
        Integer id = daoUserDetails.getUser().getId();
        redisCache.deleteObject("login:" + id);
        return ResponseResult.setHttpCodeEnum(HttpCodeEnum.SUCCESS);
    }
}
