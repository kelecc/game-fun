package top.kelecc.model.user.dto;


import lombok.Data;

/**
 * @author 可乐
 */
@Data
public class WmUserLoginDto {

    /**
     * 用户名
     */
    private String name;
    /**
     * 密码
     */
    private String password;
}
