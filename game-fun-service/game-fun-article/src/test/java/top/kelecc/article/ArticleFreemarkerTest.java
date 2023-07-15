package top.kelecc.article;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.kelecc.article.mapper.ApArticleContentMapper;
import top.kelecc.article.service.ArticleService;
import top.kelecc.file.starter.service.FileStorageService;
import top.kelecc.model.article.pojo.ApArticle;
import top.kelecc.model.article.pojo.ApArticleContent;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;

/**
 * @author 可乐
 * @version 1.0
 * @description:
 * @date 2023/7/15 15:35
 */
@SpringBootTest
@Slf4j
public class ArticleFreemarkerTest {
    @Resource
    private ApArticleContentMapper apArticleContentMapper;
    @Resource
    private Configuration configuration;
    @Resource
    private FileStorageService fileStorageService;
    @Resource
    private ArticleService articleService;

    @Test
    public void createArticleHtml() throws Exception {
        List<ApArticleContent> list = apArticleContentMapper.selectList(null);
        Assertions.assertNotNull(list);
        list.forEach(apArticleContent -> {
            try {
                try (Writer writer = createHtml(apArticleContent.getContent())) {
                    InputStream in = new ByteArrayInputStream(writer.toString().getBytes());
                    String url = fileStorageService.uploadHtmlFile("articleHtml", apArticleContent.getArticleId() + ".html", in);
                    articleService.update(Wrappers.<ApArticle>lambdaUpdate()
                            .eq(ApArticle::getId, apArticleContent.getArticleId())
                            .set(ApArticle::getStaticUrl, url));
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private Writer createHtml(String content) throws Exception {
        Template template = configuration.getTemplate("article.ftl");
        HashMap<String, Object> map = new HashMap<>();
        map.put("content", JSONArray.parseArray(content));
        StringWriter stringWriter = new StringWriter();
        template.process(map, stringWriter);
        return stringWriter;
    }
}
