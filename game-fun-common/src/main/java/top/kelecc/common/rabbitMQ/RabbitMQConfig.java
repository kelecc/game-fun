package top.kelecc.common.rabbitMQ;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitOperations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.amqp.RabbitTemplateConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.kelecc.common.constants.RabbitMQConstants;


/**
 * @author 可乐
 */
@EnableRabbit
@Configuration
@AutoConfigureBefore({RabbitAutoConfiguration.class, ReturnCallback.class})
public class RabbitMQConfig {

    @Bean
    @ConditionalOnSingleCandidate(ConnectionFactory.class)
    @ConditionalOnMissingBean({RabbitOperations.class})
    public RabbitTemplate rabbitTemplate(RabbitTemplateConfigurer configurer, ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate();
        configurer.configure(template, connectionFactory);
        template.setMessageConverter(new FastJsonMessageConverter());
        return template;
    }

    /**
     * 配置接收的消息转换器
     *
     * @return
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new FastJsonMessageConverter();
    }

    /**
     * 文章上下架队列
     * @return
     */
    @Bean
    public Queue articleUpOrDownQueue() {
        return new Queue(RabbitMQConstants.QUEUE_WEMEDIA_UP_OR_DOWN_ARTICLE, true, false, false);
    }

}
