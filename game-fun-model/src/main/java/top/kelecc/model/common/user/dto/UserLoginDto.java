package top.kelecc.model.common.user.dto;

import lombok.Data;

/**
 * @author 可乐
 * @version 1.0
 * @description:
 * @date 2023/7/12 16:10
 */
@Data
public class UserLoginDto {
    /**
     * 密码明文
     */
    private String password;

    /**
     * 手机号
     */
    private String phone;
}
