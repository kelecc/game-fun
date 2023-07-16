package top.kelecc.security.component;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import top.kelecc.model.user.pojo.WmUser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author 可乐
 * @version 1.0
 * @description:
 * @date 2023/5/17 14:37
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class WmUserDetails implements UserDetails {
    private WmUser user;
    private List<String> permissions;
    @JSONField(serialize = false)
    private List<GrantedAuthority> authorities;

    public WmUserDetails(WmUser user, List<String> permissions) {
        this.user = user;
        this.permissions = permissions;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (authorities != null) {
            return authorities;
        }
        authorities = new ArrayList<>();
        for (String permission : permissions) {
            authorities.add(new SimpleGrantedAuthority(permission));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return user.getStatus() == 9;
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.getStatus() == 9;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return user.getStatus() == 9;
    }

    @Override
    public boolean isEnabled() {
        return user.getStatus() == 9;
    }
}
