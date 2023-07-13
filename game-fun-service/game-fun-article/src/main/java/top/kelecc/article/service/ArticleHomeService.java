package top.kelecc.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.kelecc.model.article.dtos.ArticleHomeDto;
import top.kelecc.model.article.pojo.ApArticle;

import java.util.List;

/**
 * @author 可乐
 * @version 1.0
 * @description:
 * @date 2023/7/13 17:14
 */
public interface ArticleHomeService extends IService<ApArticle> {
    /**
     * 加载首页文章
     *
     * @param type
     * @param dto
     * @return
     */
    List<ApArticle> load(Short type, ArticleHomeDto dto);
}
