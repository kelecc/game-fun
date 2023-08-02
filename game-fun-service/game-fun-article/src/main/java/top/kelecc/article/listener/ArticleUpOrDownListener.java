package top.kelecc.article.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import top.kelecc.article.service.ArticleConfigService;
import top.kelecc.common.constants.RabbitMQConstants;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author 可乐
 * @version 1.0
 * @description: 文章上下架监听器, 消费失败默认丢弃消息。
 * @date 2023/8/2 15:50
 */
@Component
@Slf4j
public class ArticleUpOrDownListener {
    @Resource
    private ArticleConfigService articleConfigService;

    @RabbitListener(queues = {RabbitMQConstants.QUEUE_WEMEDIA_UP_OR_DOWN_ARTICLE})
    public void listener01(Map<String, Object> map) {
        try {
            if (map != null) {
                long id = (long) map.get("articleId");
                short enable = (short) map.get("enable");
                log.info("listener01收到上下架消息，文章id：{}，上下架状态：{}", id, enable);
                articleConfigService.upOrDownArticle(id, enable);
            }
        } catch (Exception e) {
            log.error("listener01文章上下架失败，原因：{}", e.getMessage());
        }
    }

    @RabbitListener(queues = {RabbitMQConstants.QUEUE_WEMEDIA_UP_OR_DOWN_ARTICLE})
    public void listener02(Map<String, Object> map) {
        try {
            if (map != null) {
                long id = (long) map.get("articleId");
                short enable = (short) map.get("enable");
                log.info("listener02收到上下架消息，文章id：{}，上下架状态：{}", id, enable);
                articleConfigService.upOrDownArticle(id, enable);
            }
        } catch (Exception e) {
            log.error("listener02文章上下架失败，原因：{}", e.getMessage());
        }
    }
}
