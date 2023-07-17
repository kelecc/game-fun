package top.kelecc.model.user.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author 可乐
 */
@Data
public class WmUserLoginDto {

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名", dataType = "String", example = "admin", required = true)
    @NotBlank(message = "用户名不能为空")
    private String name;
    /**
     * 密码
     */
    @ApiModelProperty(value = "密码", dataType = "String", example = "123456", required = true)
    @NotBlank(message = "密码不能为空")
    private String password;
}
