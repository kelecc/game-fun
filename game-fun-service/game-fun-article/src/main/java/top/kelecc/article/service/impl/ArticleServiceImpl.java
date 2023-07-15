package top.kelecc.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.kelecc.article.mapper.ApArticleMapper;
import top.kelecc.article.service.ArticleService;
import top.kelecc.model.article.pojo.ApArticle;

import javax.annotation.Resource;

/**
 * @author 可乐
 * @version 1.0
 * @description:
 * @date 2023/7/15 16:14
 */
@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService {
    @Resource
    private ApArticleMapper apArticleMapper;

    @Override
    public int update(LambdaUpdateWrapper<ApArticle> lambdaUpdateWrapper) {
        return apArticleMapper.update(null, lambdaUpdateWrapper);
    }
}
