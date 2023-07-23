package top.kelecc.model.article.dtos;

import lombok.Data;
import top.kelecc.model.article.pojo.ApArticle;

import javax.validation.constraints.NotBlank;

/**
 * @author 可乐
 * @version 1.0
 * @description:
 * @date 2023/7/23 16:11
 */
@Data
public class ArticleDto extends ApArticle {
    /**
     * 文章内容
     */
    @NotBlank(message = "文章内容不能为空")
    private String content;
}
