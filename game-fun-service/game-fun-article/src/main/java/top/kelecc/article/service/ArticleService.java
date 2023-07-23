package top.kelecc.article.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import top.kelecc.model.article.dtos.ArticleDto;
import top.kelecc.model.article.pojo.ApArticle;
import top.kelecc.model.common.dtos.ResponseResult;

/**
 * @author 可乐
 * @version 1.0
 * @description:
 * @date 2023/7/15 16:13
 */
public interface ArticleService {
    /**
     * 更新文章
     * @param lambdaUpdateWrapper
     * @return
     */
    int update(LambdaUpdateWrapper<ApArticle> lambdaUpdateWrapper);

    /**
     * 保存app端相关文章
     * @param dto
     * @return
     */
    ResponseResult saveArticle(ArticleDto dto) ;
}
