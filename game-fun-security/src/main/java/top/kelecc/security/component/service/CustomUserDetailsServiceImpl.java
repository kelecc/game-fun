package top.kelecc.security.component.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import top.kelecc.model.user.pojo.ApUser;
import top.kelecc.model.user.pojo.WmUser;
import top.kelecc.security.component.AppUserDetails;
import top.kelecc.security.component.WmUserDetails;
import top.kelecc.security.constants.UserTypeConstans;
import top.kelecc.security.dao.AppUserMapper;
import top.kelecc.security.dao.WeMediaUserMapper;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Objects;

/**
 * @author 可乐
 * @version 1.0
 * @description:
 * @date 2023/7/16 14:29
 */
@Service
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {
    @Resource
    private AppUserMapper appUserMapper;
    @Resource
    private WeMediaUserMapper weMediaUserMapper;

    @Override
    public UserDetails loadUserByUsernameAndUserType(String username, String userType) throws UsernameNotFoundException {
        if (UserTypeConstans.APP_USER.equals(userType)) {
            QueryWrapper<ApUser> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.eq("phone", username);
            ApUser user = appUserMapper.selectOne(userQueryWrapper);
            if (Objects.isNull(user)) {
                throw new RuntimeException("用户不存在！");
            }
            //Todo 封装用户权限 List<String> permissions = userMapper.selectPermissionByUserId(user.getId());
            return new AppUserDetails(user, new ArrayList<>());
        } else if (UserTypeConstans.WE_MEDIA_USER.equals(userType)) {
            QueryWrapper<WmUser> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.eq("name", username);
            WmUser wmUser = weMediaUserMapper.selectOne(userQueryWrapper);
            if (Objects.isNull(wmUser)) {
                throw new RuntimeException("用户不存在！");
            }
            return new WmUserDetails(wmUser, new ArrayList<>());
        } else {
            return null;
        }
    }
}
