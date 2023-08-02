package top.kelecc.weMedia.service;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import top.kelecc.model.weMedia.pojo.WmNews;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author 可乐
 * @version 1.0
 * @description:
 * @date 2023/8/1 21:53
 */
@SpringBootTest
public class RabbitMqTest {
    @Resource
    private RabbitTemplate rabbitTemplate;

    @Test
    void publishTest() {
        WmNews wmNews = new WmNews();
        wmNews.setId(10086);
        wmNews.setPublishTime(new Date());
        rabbitTemplate.convertAndSend("queue01", wmNews);
    }
}
