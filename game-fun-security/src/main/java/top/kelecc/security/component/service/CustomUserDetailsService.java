package top.kelecc.security.component.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author 可乐
 * @version 1.0
 * @description: 多类型用户登录
 * @date 2023/7/16 13:50
 */
public interface CustomUserDetailsService {
    /**
     * 根据用户名和用户类型查询用户信息
     * @param username 用户名
     * @param userType 用户类型
     * @return UserDetails
     * @throws UsernameNotFoundException
     */
    UserDetails loadUserByUsernameAndUserType(String username, String userType) throws UsernameNotFoundException;
}
