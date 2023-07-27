package top.kelecc.api.article.fallback;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.kelecc.api.article.IArticleClient;
import top.kelecc.model.article.dtos.ArticleDto;
import top.kelecc.model.common.dtos.ResponseResult;
import top.kelecc.model.common.enums.HttpCodeEnum;

/**
 * @author 可乐
 * @version 1.0
 * @description:
 * @date 2023/7/26 22:52
 */
@Slf4j
@Component
public class IArticleClientFallbackFactory implements FallbackFactory<IArticleClient> {
    @Override
    public IArticleClient create(Throwable throwable) {
        return new IArticleClient() {
            @Override
            public ResponseResult saveArticle(ArticleDto dto) {
                log.error("调用文章服务保存文章失败！", throwable);
                return ResponseResult.errorResult(HttpCodeEnum.SERVER_ERROR, "调用文章服务保存文章失败！");
            }
        };
    }
}
