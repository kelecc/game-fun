package top.kelecc.security.component.filter;

import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import top.kelecc.common.constants.RedisKeyConstants;
import top.kelecc.common.redis.RedisCache;
import top.kelecc.security.constants.SecurityMapKeyConstants;
import top.kelecc.security.constants.UserTypeConstans;
import top.kelecc.utils.common.JwtUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author 可乐
 * @version 1.0
 * @description:
 * @date 2023/5/17 16:41
 */
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private RedisCache redisCache;

    public JwtAuthenticationTokenFilter(RedisCache redisCache) {
        this.redisCache = redisCache;
    }

    /**
     * @param httpServletRequest
     * @param httpServletResponse
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        // 获取请求头中的token
        String token = httpServletRequest.getHeader(SecurityMapKeyConstants.AUTH_HEADER_KEY);
        // 判断token是否为空
        if (null == token) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }
        //解析token
        String jsonData = null;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            jsonData = (String) claims.getSubject();
        } catch (Exception e) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }
        HashMap<String, Object> map = JSON.parseObject(jsonData, HashMap.class);
        // 将userId存入请求中
        httpServletRequest.setAttribute(SecurityMapKeyConstants.ID_KEY, map.get(SecurityMapKeyConstants.ID_KEY));
        if (map.isEmpty() || !map.containsKey(SecurityMapKeyConstants.ID_KEY) || !map.containsKey(SecurityMapKeyConstants.USER_TYPE_KEY)) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }
        String redisKey = null;
        switch ((String) map.get(SecurityMapKeyConstants.USER_TYPE_KEY)) {
            case UserTypeConstans.APP_USER:
                redisKey = RedisKeyConstants.LOGIN_APPUSER + map.get(SecurityMapKeyConstants.ID_KEY);
                break;
            case UserTypeConstans.WE_MEDIA_USER:
                redisKey = RedisKeyConstants.LOGIN_WEMEDIAUSER + map.get(SecurityMapKeyConstants.ID_KEY);
                break;
            default:
                filterChain.doFilter(httpServletRequest, httpServletResponse);
                return;
        }
        // 从redis中获取UserDetails
        UserDetails userDetails = redisCache.getCacheObject(redisKey);
        // 判断daoUserDetails是否为空
        if (null == userDetails) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }
        // 将daoUserDetails存入SecurityContext中
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
