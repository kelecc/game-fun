package top.kelecc.model.common.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

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
    @ApiModelProperty(value = "密码明文" ,dataType = "String",required = true)
    @NotBlank(message = "密码不能为空")
    private String password;

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号" ,dataType = "String",required = true)
    @NotBlank(message = "手机号不能为空")
    private String phone;
}
