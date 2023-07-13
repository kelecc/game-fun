package top.kelecc.article.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.kelecc.article.mapper.ApArticleMapper;
import top.kelecc.article.service.ArticleHomeService;
import top.kelecc.model.article.dtos.ArticleHomeDto;
import top.kelecc.model.article.pojo.ApArticle;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 可乐
 * @version 1.0
 * @description:
 * @date 2023/7/13 17:15
 */
@Service
@Transactional
@Slf4j
public class ArticleHomeServiceImpl extends ServiceImpl<ApArticleMapper, ApArticle> implements ArticleHomeService {
    @Resource
    private ApArticleMapper apArticleMapper;

    @Override
    public List<ApArticle> load(Short type, ArticleHomeDto dto) {
        return apArticleMapper.loadArticleList(dto, type);
    }


}
