package top.kelecc.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 可乐
 * @version 1.0
 * @description:
 * @date 2023/7/11 20:40
 */
@SpringBootApplication(scanBasePackages = "top.kelecc")
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
