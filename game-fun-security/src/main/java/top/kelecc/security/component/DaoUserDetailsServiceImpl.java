package top.kelecc.security.component;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import top.kelecc.model.common.user.pojos.ApUser;
import top.kelecc.security.dao.UserMapper;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Objects;

/**
 * @author 可乐
 * @version 1.0
 * @description:
 * @date 2023/5/17 14:34
 */
@Component
public class DaoUserDetailsServiceImpl implements UserDetailsService {
    @Resource
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        QueryWrapper<ApUser> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("phone", phone);
        ApUser user = userMapper.selectOne(userQueryWrapper);
        if (Objects.isNull(user)) {
            throw new RuntimeException("手机号或密码错误");
        }
        //Todo 封装用户权限
        //List<String> permissions = userMapper.selectPermissionByUserId(user.getId());
        return new DaoUserDetails(user, new ArrayList<>());
    }
}
