package top.kelecc.weMedia;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 可乐
 * @version 1.0
 * @description:
 * @date 2023/7/17 9:42
 */
@MapperScan("top.kelecc.weMedia.mapper")
@SpringBootApplication(scanBasePackages = "top.kelecc")
public class WeMediaApplication {
    public static void main(String[] args) {
        SpringApplication.run(WeMediaApplication.class, args);
    }
}
