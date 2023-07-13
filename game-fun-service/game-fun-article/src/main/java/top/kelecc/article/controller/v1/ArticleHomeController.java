package top.kelecc.article.controller.v1;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.kelecc.article.service.ArticleHomeService;
import top.kelecc.common.constants.ArticleConstants;
import top.kelecc.model.article.dtos.ArticleHomeDto;
import top.kelecc.model.article.pojo.ApArticle;
import top.kelecc.model.common.dtos.ResponseResult;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 可乐
 * @version 1.0
 * @description:
 * @date 2023/7/13 17:13
 */
@RestController
@RequestMapping("/api/v1/article")
@Api(value = "首页文章模块", tags = "首页文章模块")
public class ArticleHomeController {
    @Resource
    private ArticleHomeService articleHomeService;

    @PostMapping("/load")
    @ApiOperation("加载首页文章")
    public ResponseResult load(@RequestBody @Validated ArticleHomeDto dto) {
        List<ApArticle> articles = articleHomeService.load(ArticleConstants.LOADTYPE_LOAD_NEW, dto);
        return ResponseResult.okResult(articles);
    }

    @PostMapping("/loadnew")
    @ApiOperation("下拉刷新加载最新文章")
    public ResponseResult loadNew(@RequestBody @Validated ArticleHomeDto dto) {
        List<ApArticle> articles = articleHomeService.load(ArticleConstants.LOADTYPE_LOAD_NEW, dto);
        return ResponseResult.okResult(articles);
    }

    @PostMapping("/loadmore")
    @ApiOperation("加载更多文章")
    public ResponseResult loadMore(@RequestBody @Validated ArticleHomeDto dto) {
        List<ApArticle> articles = articleHomeService.load(ArticleConstants.LOADTYPE_LOAD_MORE, dto);
        return ResponseResult.okResult(articles);
    }
}
