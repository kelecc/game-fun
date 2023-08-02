package top.kelecc.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.kelecc.article.mapper.ApArticleConfigMapper;
import top.kelecc.model.article.pojo.ApArticleConfig;

/**
 * @author 可乐
 * @version 1.0
 * @description:
 * @date 2023/8/2 15:51
 */
public interface ArticleConfigService{
    /**
     * 上下架文章
     * @param id
     * @param enable
     */
    void upOrDownArticle(Long id, Short enable);
}
