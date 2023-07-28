package top.kelecc.article.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.kelecc.article.service.ArticleFreemarkerService;
import top.kelecc.article.service.ArticleService;
import top.kelecc.file.starter.service.FileStorageService;
import top.kelecc.model.article.pojo.ApArticle;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;

/**
 * @author 可乐
 * @version 1.0
 * @description:
 * @date 2023/7/28 20:18
 */
@Service
@Slf4j
@Transactional
public class ArticleFreemarkerServiceImpl implements ArticleFreemarkerService {
    @Resource
    private Configuration configuration;
    @Resource
    private FileStorageService fileStorageService;
    @Resource
    private ArticleService articleService;

    @Override
    @Async
    public void buildArticleToMinio(ApArticle apArticle, String content) {
        if (StringUtils.isNotBlank(content)) {
            // 1.生成静态化内容
            Template template = null;
            StringWriter stringWriter = new StringWriter();
            try {
                template = configuration.getTemplate("article.ftl");
                HashMap<String, Object> map = new HashMap<>();
                map.put("content", JSONArray.parseArray(content));
                template.process(map, stringWriter);
            } catch (Exception e) {
                log.error("生成文章静态化内容失败:{}", e.getMessage());
                throw new RuntimeException(e);
            }
            // 2.上传到minio
            InputStream in = new ByteArrayInputStream(stringWriter.toString().getBytes());
            String url = fileStorageService.uploadHtmlFile("articleHtml", apArticle.getId() + ".html", in);
            // 3.更新文章的静态化地址
            articleService.update(Wrappers.<ApArticle>lambdaUpdate()
                    .eq(ApArticle::getId, apArticle.getId())
                    .set(ApArticle::getStaticUrl, url));
        }
    }
}
