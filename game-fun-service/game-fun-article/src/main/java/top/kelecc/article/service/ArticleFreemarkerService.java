package top.kelecc.article.service;

import top.kelecc.model.article.pojo.ApArticle;

/**
 * @author 可乐
 * @version 1.0
 * @description:
 * @date 2023/7/28 20:17
 */
public interface ArticleFreemarkerService {
    /**
     * 生成静态文件上传到minIO中
     * @param apArticle
     * @param content
     */
    public void buildArticleToMinio(ApArticle apArticle, String content);
}
