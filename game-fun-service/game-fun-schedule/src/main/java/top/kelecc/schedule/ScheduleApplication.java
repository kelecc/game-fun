package top.kelecc.schedule;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author 可乐
 * @version 1.0
 * @description:
 * @date 2023/7/30 17:24
 */
@SpringBootApplication(scanBasePackages = {"top.kelecc"})
@MapperScan("top.kelecc.schedule.mapper")
@EnableScheduling
public class ScheduleApplication {
    public static void main(String[] args) {
        SpringApplication.run(ScheduleApplication.class, args);
    }

    /**
     * mybatis-plus乐观锁支持
     *
     * @return
     */
    @Bean
    public MybatisPlusInterceptor optimisticLockerInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return interceptor;
    }
}
