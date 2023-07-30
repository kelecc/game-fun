package top.kelecc.user.service.impl;

import com.alibaba.fastjson.JSON;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import top.kelecc.common.constants.RedisKeyConstants;
import top.kelecc.common.redis.RedisCache;
import top.kelecc.model.common.dtos.ResponseResult;
import top.kelecc.model.common.enums.HttpCodeEnum;
import top.kelecc.model.user.pojo.WmUser;
import top.kelecc.model.user.vo.AppUserVo;
import top.kelecc.security.component.AppUserDetails;
import top.kelecc.security.component.WmUserDetails;
import top.kelecc.security.constants.SecurityMapKeyConstants;
import top.kelecc.security.constants.UserTypeConstans;
import top.kelecc.user.service.LoginService;
import top.kelecc.utils.common.JwtUtil;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author 可乐
 * @version 1.0
 * @description:
 * @date 2023/5/17 15:06
 */
@Service
public class LoginServiceImpl implements LoginService {
    @Resource
    private RedisCache redisCache;
    @Resource
    private AuthenticationManager authenticationManager;

    /**
     * 登录
     *
     * @param phone
     * @param password
     * @return
     */
    @Override
    public ResponseResult login(String phone, String password, String userType) {
        //认证
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(phone, password);
        HashMap<Object, Object> detailMap = new HashMap<>();
        detailMap.put(SecurityMapKeyConstants.USER_TYPE_KEY, userType);
        usernamePasswordAuthenticationToken.setDetails(detailMap);
        Authentication authenticate = null;
        try {
            authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        } catch (AuthenticationException e) {
            return ResponseResult.errorResult(HttpCodeEnum.AUTH_FAILED, e.getMessage());
        }
        UserDetails userDetails = (UserDetails) authenticate.getPrincipal();

        //生成jwt
        Integer userId = getUserIdByUserDetails(userDetails, userType);
        HashMap<String, Object> dataMap = new HashMap<>(2);
        dataMap.put(SecurityMapKeyConstants.ID_KEY, userId);
        dataMap.put(SecurityMapKeyConstants.USER_TYPE_KEY, userType);
        String jwt = JwtUtil.createJWT(JSON.toJSONString(dataMap));
        //将authentication存入redis
        String redisKey = null;
        if (UserTypeConstans.APP_USER.equals(userType)) {
            redisKey = RedisKeyConstants.LOGIN_APPUSER + userId;
        } else if (UserTypeConstans.WE_MEDIA_USER.equals(userType)) {
            redisKey = RedisKeyConstants.LOGIN_WEMEDIAUSER + userId;
        } else {
            throw new RuntimeException("暂不支持的用户类型");
        }
        redisCache.setCacheObject(redisKey, userDetails, 7, TimeUnit.DAYS);
        //返回token
        HashMap<String, Object> map = new HashMap<>(2);
        map.put("token", jwt);
        Object vo = getVo(userDetails, userType);
        map.put("user", vo);
        return ResponseResult.okResult(map);
    }

    private Object getVo(UserDetails userDetails, String userType) {
        if (UserTypeConstans.APP_USER.equals(userType)) {
            AppUserDetails appUserDetails = (AppUserDetails) userDetails;
            return new AppUserVo(appUserDetails.getUser());
        } else if (UserTypeConstans.WE_MEDIA_USER.equals(userType)) {
            WmUserDetails wmUserDetails = (WmUserDetails) userDetails;
            WmUser user = wmUserDetails.getUser();
            user.setPassword(null);
            return user;
        } else {
            throw new RuntimeException("获取Vo失败，暂不支持的用户类型");
        }
    }

    private Integer getUserIdByUserDetails(UserDetails userDetails, String userType) {
        if (UserTypeConstans.APP_USER.equals(userType)) {
            AppUserDetails appUserDetails = (AppUserDetails) userDetails;
            return appUserDetails.getUser().getId();
        } else if (UserTypeConstans.WE_MEDIA_USER.equals(userType)) {
            WmUserDetails wmUserDetails = (WmUserDetails) userDetails;
            return wmUserDetails.getUser().getId();
        } else {
            throw new RuntimeException("获取id失败，暂不支持的用户类型");
        }
    }

    /**
     * @return
     */
    @Override
    public Object logout(String userType) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        if (UserTypeConstans.APP_USER.equals(userType)) {
            AppUserDetails appUserDetails = (AppUserDetails) userDetails;
            redisCache.deleteObject(RedisKeyConstants.LOGIN_APPUSER + appUserDetails.getUser().getId());
        } else if (UserTypeConstans.WE_MEDIA_USER.equals(userType)) {
            WmUserDetails wmUserDetails = (WmUserDetails) userDetails;
            redisCache.deleteObject(RedisKeyConstants.LOGIN_WEMEDIAUSER + wmUserDetails.getUser().getId());
        } else {
            throw new RuntimeException("暂不支持的用户类型");
        }
        return ResponseResult.setHttpCodeEnum(HttpCodeEnum.SUCCESS);
    }
}
