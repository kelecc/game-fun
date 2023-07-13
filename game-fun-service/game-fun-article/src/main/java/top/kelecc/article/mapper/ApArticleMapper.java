package top.kelecc.article.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import top.kelecc.model.article.dtos.ArticleHomeDto;
import top.kelecc.model.article.pojo.ApArticle;

import java.util.List;

/**
 * @author 可乐
 * @version 1.0
 * @description:
 * @date 2023/7/13 16:53
 */
public interface ApArticleMapper extends BaseMapper<ApArticle> {
    /**
     * 加载首页文章
     *
     * @param dto
     * @param type
     * @return
     */
    public List<ApArticle> loadArticleList(@Param("dto") ArticleHomeDto dto, @Param("type") Short type);
}
