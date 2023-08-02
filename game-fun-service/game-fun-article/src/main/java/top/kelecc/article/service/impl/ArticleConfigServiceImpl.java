package top.kelecc.article.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.kelecc.article.mapper.ApArticleConfigMapper;
import top.kelecc.article.service.ArticleConfigService;
import top.kelecc.model.article.pojo.ApArticleConfig;

/**
 * @author 可乐
 * @version 1.0
 * @description:
 * @date 2023/8/2 15:53
 */
@Service
@Slf4j
public class ArticleConfigServiceImpl extends ServiceImpl<ApArticleConfigMapper, ApArticleConfig> implements ArticleConfigService {


    @Override
    public void upOrDownArticle(Long id, Short enable) {
        boolean isDown = true;
        if (enable.equals((short) 1)) {
            isDown = false;
        }
        boolean update = update(Wrappers.<ApArticleConfig>lambdaUpdate().set(ApArticleConfig::getIsDown, isDown).eq(ApArticleConfig::getArticleId, id));

    }
}
