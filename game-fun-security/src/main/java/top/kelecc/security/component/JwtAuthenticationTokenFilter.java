package top.kelecc.security.component;

import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import top.kelecc.security.dao.UserMapper;
import top.kelecc.security.utils.RedisCache;
import top.kelecc.utils.common.JwtUtil;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 可乐
 * @version 1.0
 * @description:
 * @date 2023/5/17 16:41
 */
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Resource
    private RedisCache redisCache;
    @Resource
    private UserMapper userMapper;

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
        String token = httpServletRequest.getHeader("token");
        // 判断token是否为空
        if (null == token) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }
        //解析token
        String userId = null;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userId = (String) claims.getSubject();
        } catch (Exception e) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }
        // 从redis中获取daoUserDetails
        DaoUserDetails daoUserDetails = redisCache.getCacheObject("login:" + userId);
        // 判断daoUserDetails是否为空
        if (null == daoUserDetails) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }
        // 将daoUserDetails存入SecurityContext中
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(daoUserDetails, null, daoUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
