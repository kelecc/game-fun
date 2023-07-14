package top.kelecc.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 可乐
 * @version 1.0
 * @description:
 * @date 2023/7/13 20:53
 */
@SpringBootApplication(scanBasePackages = "top.kelecc")
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}
