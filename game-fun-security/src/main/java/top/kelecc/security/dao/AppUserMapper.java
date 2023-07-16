package top.kelecc.security.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import top.kelecc.model.user.pojo.ApUser;

/**
 * @author 可乐
 */
public interface AppUserMapper extends BaseMapper<ApUser> {
    /**
     * 查询用户权限
     *
     * @param userId 用户id
     * @return
     */
//    List<String> selectPermissionByUserId(Integer userId);
}
