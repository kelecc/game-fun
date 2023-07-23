package top.kelecc.article.feign;

import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import top.kelecc.api.article.IArticleClient;
import top.kelecc.article.service.ArticleService;
import top.kelecc.model.article.dtos.ArticleDto;
import top.kelecc.model.common.dtos.ResponseResult;

import javax.annotation.Resource;

/**
 * @author 可乐
 * @version 1.0
 * @description:
 * @date 2023/7/23 16:49
 */
@RestController
public class ArticleClient implements IArticleClient {
    @Resource
    private ArticleService articleService;

    @Override
    @PostMapping("/api/v1/article/save")
    @ApiOperation("保存文章")
    public ResponseResult saveArticle(@RequestBody @Validated ArticleDto dto) {
        return articleService.saveArticle(dto);
    }
}
