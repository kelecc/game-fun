package top.kelecc.security.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import top.kelecc.security.component.CustomAuthenticationProvider;
import top.kelecc.security.component.filter.JwtAuthenticationTokenFilter;
import top.kelecc.security.component.service.CustomUserDetailsService;
import top.kelecc.security.dao.AppUserMapper;
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
    private CustomUserDetailsService customUserDetailsService;
    @Resource
    private RedisCache redisCache;
    @Resource
    private AppUserMapper userMapper;
    @Value("${security.not-auth-path:/api/v1/public/**,/api/v2/public/**, /login/in , /api/v1/login/login_auth/**, /v2/api-docs/**, /swagger-resources/**, /doc.html**}")
    private String[] notAuthPath;
    @Value("${security.static-path:/webjars/**, /service-worker.js, /static/**, /css/**, /js/**, /img/**, /fonts/**, /favicon.ico}")
    private String[] staticPath;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public AuthenticationProvider customAuthenticationProvider() {
        CustomAuthenticationProvider customAuthenticationProvider = new CustomAuthenticationProvider();
        customAuthenticationProvider.setCustomUserDetailsService(customUserDetailsService);
        customAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
        customAuthenticationProvider.setHideUserNotFoundExceptions(false);
        return customAuthenticationProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customAuthenticationProvider());
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
                .antMatchers(this.notAuthPath).permitAll()
                // 各个服务之间放行
                .antMatchers("/**").hasIpAddress("192.168.199.1")
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated();
        http.addFilterBefore(new JwtAuthenticationTokenFilter(redisCache), UsernamePasswordAuthenticationFilter.class);
        http.cors();
        http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(this.staticPath);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
