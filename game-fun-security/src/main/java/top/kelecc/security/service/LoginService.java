package top.kelecc.security.service;

/**
 * @author 可乐
 */
public interface LoginService {
    /**
     * 登录
     *
     * @param username
     * @param password
     * @return
     */
    Object login(String username, String password);

    Object logout();
}
