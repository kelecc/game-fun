package top.kelecc.model.common.user.vo;

import lombok.Data;
import top.kelecc.model.common.user.pojos.ApUser;

import java.util.Date;

/**
 * @author 可乐
 * @version 1.0
 * @description:
 * @date 2023/7/12 16:05
 */
@Data
public class UserVo {
    public UserVo(ApUser user) {
        this.createdTime = user.getCreatedTime();
        this.flag = user.getFlag();
        this.id = user.getId();
        this.identityAuthentication = user.getIdentityAuthentication();
        this.image = user.getImage();
        this.name = user.getName();
        this.status = user.getStatus();
        this.sex = user.getSex();
        this.phone = user.getPhone();
    }

    /**
     * 主键
     */
    private Integer id;

    /**
     * 用户名
     */
    private String name;


    /**
     * 手机号
     */
    private String phone;

    /**
     * 头像
     */
    private String image;

    /**
     * 0 男
     * 1 女
     * 2 未知
     */
    private Short sex;

    /**
     * 是否身份认证
     */
    private Boolean identityAuthentication;

    /**
     * 0正常
     * 1锁定
     */
    private Boolean status;

    /**
     * 0 普通用户
     * 1 博主
     * 2 大V
     */
    private Short flag;

    /**
     * 注册时间
     */
    private Date createdTime;
}
