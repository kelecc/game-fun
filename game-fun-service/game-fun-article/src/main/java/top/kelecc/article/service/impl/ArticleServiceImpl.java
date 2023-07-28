package top.kelecc.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import top.kelecc.article.mapper.ApArticleConfigMapper;
import top.kelecc.article.mapper.ApArticleContentMapper;
import top.kelecc.article.mapper.ApArticleMapper;
import top.kelecc.article.service.ArticleFreemarkerService;
import top.kelecc.article.service.ArticleService;
import top.kelecc.model.article.dtos.ArticleDto;
import top.kelecc.model.article.pojo.ApArticle;
import top.kelecc.model.article.pojo.ApArticleConfig;
import top.kelecc.model.article.pojo.ApArticleContent;
import top.kelecc.model.common.dtos.ResponseResult;

import javax.annotation.Resource;

/**
 * @author 可乐
 * @version 1.0
 * @description:
 * @date 2023/7/15 16:14
 */
@Service
@Slf4j
public class ArticleServiceImpl extends ServiceImpl<ApArticleMapper, ApArticle> implements ArticleService {
    @Resource
    private ApArticleMapper apArticleMapper;
    @Resource
    private ApArticleConfigMapper apArticleConfigMapper;
    @Resource
    private ApArticleContentMapper apArticleContentMapper;
    @Resource
    private ArticleFreemarkerService articleFreemarkerService;


    @Override
    public int update(LambdaUpdateWrapper<ApArticle> lambdaUpdateWrapper) {
        return apArticleMapper.update(null, lambdaUpdateWrapper);
    }

    @Override
    public ResponseResult saveArticle(ArticleDto dto) {
        ApArticle apArticle = new ApArticle();
        BeanUtils.copyProperties(dto, apArticle);
        // 1.判断id是否存在，存在则更新，不存在则新增
        if (dto.getId() == null) {
            // 2.新增
            save(apArticle);
            // 保存配置
            ApArticleConfig apArticleConfig = new ApArticleConfig(apArticle.getId());
            apArticleConfigMapper.insert(apArticleConfig);

            // 保存文章内容
            ApArticleContent apArticleContent = new ApArticleContent();
            apArticleContent.setArticleId(apArticle.getId());
            apArticleContent.setContent(dto.getContent());
            apArticleContentMapper.insert(apArticleContent);
        } else {
            // 3.更新
            updateById(apArticle);
            // 修改文章内容
            ApArticleContent apArticleContent = apArticleContentMapper.selectOne(Wrappers.<ApArticleContent>lambdaQuery().eq(ApArticleContent::getArticleId, dto.getId()));
            apArticleContent.setContent(dto.getContent());
            apArticleContentMapper.updateById(apArticleContent);
        }
        // 4.异步生成文章静态化页面
        articleFreemarkerService.buildArticleToMinio(apArticle, dto.getContent());
        return ResponseResult.okResult(apArticle.getId());
    }
}
