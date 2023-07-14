package top.kelecc.user.service;

import top.kelecc.model.common.dtos.ResponseResult;

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
    ResponseResult login(String username, String password);

    Object logout();
}
