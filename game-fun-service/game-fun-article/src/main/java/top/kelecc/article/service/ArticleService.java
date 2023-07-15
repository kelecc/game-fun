package top.kelecc.article.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import top.kelecc.model.article.pojo.ApArticle;

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
}
