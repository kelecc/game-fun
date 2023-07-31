package top.kelecc.common.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 可乐
 */
@Configuration
public class RedissonConfig {
    @Value("${spring.redis.host}")
    private String redisAddress;
    @Value("${spring.redis.password}")
    private String password;
    @Value("${spring.redis.port}")
    private String port;

    @Bean
    public RedissonClient redissonClient() {
        // 默认连接地址 127.0.0.1:6379
        RedissonClient redisson = Redisson.create();
        Config config = new Config();
        config.useSingleServer().setAddress("redis://" + redisAddress + ":" + port)
                .setUsername("")
                .setPassword(password);
        return redisson;
    }
}
