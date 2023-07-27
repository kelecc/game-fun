package top.kelecc.api.article;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import top.kelecc.api.article.fallback.IArticleClientFallbackFactory;
import top.kelecc.model.article.dtos.ArticleDto;
import top.kelecc.model.common.dtos.ResponseResult;

/**
 * @author 可乐
 * @version 1.0
 * @description:
 * @date 2023/7/23 16:26
 */
@FeignClient(value = "game-fun-article", fallbackFactory = IArticleClientFallbackFactory.class)
public interface IArticleClient {

    /**
     * 保存文章
     * @param dto 文章dto
     * @return
     */
    @PostMapping("/api/v1/article/save")
    public ResponseResult saveArticle(@RequestBody ArticleDto dto);
}
