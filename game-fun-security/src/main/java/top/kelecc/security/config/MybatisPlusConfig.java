package top.kelecc.security.config;

import com.baomidou.mybatisplus.core.config.GlobalConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisPlusConfig {
    @Bean
    public GlobalConfig globalConfig() {
        GlobalConfig globalConfig = new GlobalConfig();
        // 设置驼峰转下划线
        globalConfig.setDbConfig(new GlobalConfig.DbConfig().setTableUnderline(true));
        return globalConfig;
    }
}
