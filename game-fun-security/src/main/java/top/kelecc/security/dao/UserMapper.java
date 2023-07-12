package top.kelecc.security.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import top.kelecc.model.common.user.pojos.ApUser;

/**
 * @author 可乐
 */
public interface UserMapper extends BaseMapper<ApUser> {
    /**
     * 查询用户权限
     *
     * @param userId 用户id
     * @return
     */
//    List<String> selectPermissionByUserId(Integer userId);
}
