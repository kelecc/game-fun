package top.kelecc.security.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import top.kelecc.security.component.JwtAuthenticationTokenFilter;
import top.kelecc.security.dao.UserMapper;
import top.kelecc.security.utils.RedisCache;

import javax.annotation.Resource;

/**
 * @author 可乐
 * @version 1.0
 * @description:
 * @date 2023/5/17 14:50
 */
@EnableGlobalMethodSecurity(prePostEnabled = true)
@MapperScan("top.kelecc.security.dao")
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Resource
    private AuthenticationEntryPoint authenticationEntryPoint;
    @Resource
    private RedisCache redisCache;
    @Resource
    private UserMapper userMapper;
    private final String[] NOT_AUTH_PATH = {
            "/favicon.ico",
            "/service-worker.js",
            "/api/v1/login",
            "/v2/api-docs/**",
            "/swagger-resources/**",
            "/doc.html",
            "/webjars/**"
    };
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //关闭csrf
                .csrf().disable()
                //不通过Session获取SecurityContext
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // 对于登录接口 允许匿名访问
                .antMatchers(this.NOT_AUTH_PATH).anonymous()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated();
        http.addFilterBefore(new JwtAuthenticationTokenFilter(redisCache,userMapper), UsernamePasswordAuthenticationFilter.class);
        http.cors();
        http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(this.NOT_AUTH_PATH);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
