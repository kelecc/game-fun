package top.kelecc.weMedia.service.impl;

import com.alibaba.fastjson.JSONArray;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.kelecc.api.article.IArticleClient;
import top.kelecc.baiduAi.BaiDuAiConstants;
import top.kelecc.baiduAi.ImgAndTextCensor;
import top.kelecc.file.starter.service.FileStorageService;
import top.kelecc.model.article.dtos.ArticleDto;
import top.kelecc.model.common.dtos.ResponseResult;
import top.kelecc.model.user.pojo.WmUser;
import top.kelecc.model.weMedia.pojo.WmChannel;
import top.kelecc.model.weMedia.pojo.WmNews;
import top.kelecc.security.dao.WeMediaUserMapper;
import top.kelecc.weMedia.mapper.WmChannelMapper;
import top.kelecc.weMedia.mapper.WmNewsMapper;
import top.kelecc.weMedia.service.WmNewsAutoScanService;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 可乐
 * @version 1.0
 * @description:
 * @date 2023/7/26 10:57
 */
@Slf4j
@Service
public class WmNewsAutoScanServiceImpl implements WmNewsAutoScanService {

    @Resource
    private WmNewsMapper wmNewsMapper;
    @Resource
    private FileStorageService fileStorageService;
    @Resource
    private ImgAndTextCensor imgAndTextCensor;
    @Resource
    private IArticleClient articleClient;
    @Resource
    private WmChannelMapper wmChannelMapper;
    @Resource
    private WeMediaUserMapper wmUserMapper;

    @Override
    @Async
    @GlobalTransactional
    public void autoScanWmNews(Integer id) {
        WmNews wmNews = wmNewsMapper.selectById(id);
        if (wmNews == null) {
            log.error("自媒体文章审核失败，原因：文章不存在，id:{}", id);
            throw new RuntimeException("自媒体文章审核失败，原因：文章不存在，id:" + id);
        }

        // 审核状态为 submit的文章
        if (wmNews.getStatus().equals(WmNews.Status.SUBMIT.getCode())) {
            // 提取标题和文章内容和图片
            Map<String, Object> textAndImages = extractTextAndImages(wmNews);
            // 审核文本信息
            String text = textAndImages.get("title") + "-" + textAndImages.get("content");
            boolean isTextSafe = auditText(text, wmNews);
            if (!isTextSafe) {
                return;
            }
            // 审核图片信息
            boolean isImageSafe = auditImage((List<String>) textAndImages.get("images"), wmNews);
            if (!isImageSafe) {
                return;
            }
            // 审核通过，将文章保存到app端
            ResponseResult result = saveAppArticle(wmNews);
            if (!result.getCode().equals(200)) {
                throw new RuntimeException("自媒体文章审核失败，原因：保存文章到app端失败，id:" + wmNews.getId());
            }
            // 将保存得到的文章id更新到自媒体文章表中
            wmNews.setArticleId((Long) result.getData());
            updateWmNews(wmNews, (short) 9, "审核通过");
        }
    }

    private ResponseResult saveAppArticle(WmNews wmNews) {
        ArticleDto articleDto = new ArticleDto();
        BeanUtils.copyProperties(wmNews, articleDto);
        // 设置文章布局
        articleDto.setLayout(wmNews.getType());
        // 设置文章的频道名称
        WmChannel wmChannel = wmChannelMapper.selectById(wmNews.getChannelId());
        if (wmChannel != null) {
            articleDto.setChannelName(wmChannel.getName());
        }
        // 设置作者
        WmUser wmUser = wmUserMapper.selectById(wmNews.getUserId());
        if (wmUser != null) {
            articleDto.setAuthorName(wmUser.getName());
            articleDto.setAuthorId(Long.valueOf(wmUser.getId()));
        }
        // 设置文章id
        if (wmNews.getArticleId() != null) {
            articleDto.setId(wmNews.getArticleId());
        }
        articleDto.setCreatedTime(new Date());
        return articleClient.saveArticle(articleDto);
    }

    /**
     * 审核图片信息
     *
     * @param images
     * @param wmNews
     * @return
     */
    private boolean auditImage(List<String> images, WmNews wmNews) {
        if (images == null || images.size() == 0) {
            return true;
        }
        // 图片去重
        images = images.stream().distinct().collect(Collectors.toList());
        // 下载图片并审核
        boolean isSuspected = false;
        for (String image : images) {
            try {
                byte[] file = fileStorageService.downLoadFile(image);
                Map<String, Object> resultMap = imgAndTextCensor.imgCensor(file);
                Integer conclusionType = (Integer) resultMap.get("conclusionType");
                if (conclusionType == null) {
                    throw new RuntimeException("自媒体文章审核失败，原因：审核图片信息异常(百度AI审核失败)，id:" + wmNews.getId());
                }
                if (BaiDuAiConstants.CONCLUSION_TYPE_SUSPECTED.equals(conclusionType)) {
                    // 人工复审
                    isSuspected = true;
                } else if (BaiDuAiConstants.CONCLUSION_TYPE_NOT_NORMAL.equals(conclusionType)) {
                    // 审核不通过
                    updateWmNews(wmNews, (short) 2, "图片存在违禁内容，审核不通过");
                    return false;
                } else if (BaiDuAiConstants.CONCLUSION_TYPE_FAILED.equals(conclusionType)) {
                    throw new RuntimeException("自媒体文章审核失败，原因：审核图片信息异常(百度AI审核失败)，id:" + wmNews.getId());
                }
            } catch (Exception e) {
                log.error("自媒体文章审核失败，原因：审核图片信息异常(百度AI审核失败)，id:{}", wmNews.getId());
            }
        }
        if (isSuspected) {
            updateWmNews(wmNews, (short) 3, "图片存在不确定内容，需要人工复审");
            return false;
        }
        return true;
    }

    /**
     * 审核文本信息
     *
     * @param text
     * @param wmNews
     * @return
     */
    private boolean auditText(String text, WmNews wmNews) {
        if (text.length() == 0) {
            return true;
        }
        try {
            Map<String, Object> resultMap = imgAndTextCensor.textCensor(text);
            Integer conclusionType = (Integer) resultMap.get("conclusionType");
            if (conclusionType == null) {
                throw new RuntimeException("自媒体文章审核失败，原因：审核文本信息异常(百度AI审核失败)，id:" + wmNews.getId());
            }
            if (BaiDuAiConstants.CONCLUSION_TYPE_NORMAL.equals(conclusionType)) {
                // 审核通过
                return true;
            } else if (BaiDuAiConstants.CONCLUSION_TYPE_SUSPECTED.equals(conclusionType)) {
                // 人工复审
                updateWmNews(wmNews, (short) 3, "文章存在不确定内容，需要人工复审");
                return false;
            } else if (BaiDuAiConstants.CONCLUSION_TYPE_NOT_NORMAL.equals(conclusionType)) {
                // 审核不通过
                updateWmNews(wmNews, (short) 2, "文章存在违禁内容，审核不通过");
                return false;
            } else {
                throw new RuntimeException("自媒体文章审核失败，原因：审核文本信息异常(百度AI审核失败)，id:" + wmNews.getId());
            }
        } catch (Exception e) {
            log.error("自媒体文章审核失败，原因：审核文本信息异常(百度AI审核失败)，id:{}", wmNews.getId());
        }
        return false;
    }

    private void updateWmNews(WmNews wmNews, short i, String s) {
        wmNews.setStatus(i);
        wmNews.setReason(s);
        wmNewsMapper.updateById(wmNews);
    }

    /**
     * 提取文章标题、封面和内容中的文本和图片
     *
     * @param wmNews
     * @return 返回一个map，包含文章标题、封面和内容中的文本和图片
     */
    private Map<String, Object> extractTextAndImages(WmNews wmNews) {
        // 存储文章内容
        StringBuilder content = new StringBuilder();

        // 存储文章标题
        String title = wmNews.getTitle();

        // 存储文章图片
        List<String> images = new ArrayList<>();

        // 提取文章内容中的文本和图片
        if (StringUtils.isNotBlank(wmNews.getContent())) {
            List<Map> maps = JSONArray.parseArray(wmNews.getContent(), Map.class);
            for (Map map : maps) {
                if (map.get("type").equals("image")) {
                    images.add(map.get("value").toString());
                } else if (map.get("type").equals("text")) {
                    content.append(map.get("value").toString());
                }
            }
        }

        // 提取封面图片
        if (StringUtils.isNotBlank(wmNews.getImages())) {
            String[] imagesArr = wmNews.getImages().split(",");
            images.addAll(Arrays.asList(imagesArr));
        }

        Map<String, Object> result = new HashMap<>();
        result.put("content", content.toString());
        result.put("title", title);
        result.put("images", images);

        return result;
    }
}
