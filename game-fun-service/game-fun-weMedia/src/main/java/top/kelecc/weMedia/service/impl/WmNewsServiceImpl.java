package top.kelecc.weMedia.service.impl;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.kelecc.common.constants.WemediaConstants;
import top.kelecc.common.exception.CustomException;
import top.kelecc.model.common.dtos.PageResponseResult;
import top.kelecc.model.common.dtos.ResponseResult;
import top.kelecc.model.common.enums.HttpCodeEnum;
import top.kelecc.model.weMedia.dto.WmNewsDto;
import top.kelecc.model.weMedia.dto.WmNewsPageReqDto;
import top.kelecc.model.weMedia.pojo.WmMaterial;
import top.kelecc.model.weMedia.pojo.WmNews;
import top.kelecc.model.weMedia.pojo.WmNewsMaterial;
import top.kelecc.weMedia.mapper.WmMaterialMapper;
import top.kelecc.weMedia.mapper.WmNewsMapper;
import top.kelecc.weMedia.mapper.WmNewsMaterialMapper;
import top.kelecc.weMedia.service.WmNewsAutoScanService;
import top.kelecc.weMedia.service.WmNewsService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class WmNewsServiceImpl extends ServiceImpl<WmNewsMapper, WmNews> implements WmNewsService {
    @Resource
    WmNewsMaterialMapper wmNewsMaterialMapper;
    @Resource
    WmMaterialMapper wmMaterialMapper;
    @Resource
    WmNewsAutoScanService wmNewsAutoScanService;
    @Override
    public ResponseResult findByWmNewsPageReqDto(WmNewsPageReqDto dto, Integer userId) {
        Page<WmNews> page = new Page<>(dto.getPage(), dto.getSize());
        LambdaQueryWrapper<WmNews> wrapper = new LambdaQueryWrapper<>();
        if (dto.getChannelId() != null) {
            wrapper.eq(WmNews::getChannelId, dto.getChannelId());
        }
        if (dto.getStatus() != null) {
            wrapper.eq(WmNews::getStatus, dto.getStatus());
        }
        if (StringUtils.isNotBlank(dto.getKeyword())) {
            wrapper.like(WmNews::getTitle, dto.getKeyword());
        }
        if (dto.getBeginPubDate() != null && dto.getEndPubDate() != null) {
            wrapper.between(WmNews::getPublishTime, dto.getBeginPubDate(), dto.getEndPubDate());
        }
        wrapper.eq(WmNews::getUserId, userId);
        wrapper.orderByDesc(WmNews::getPublishTime);
        page = page(page, wrapper);
        PageResponseResult pageResponseResult = new PageResponseResult(dto.getPage(), dto.getSize(), (int) page.getTotal());
        pageResponseResult.setData(page.getRecords());
        return pageResponseResult;
    }

    @Override
    public ResponseResult submitNews(WmNewsDto dto, Integer userId) {
        WmNews wmNews = new WmNews();
        BeanUtils.copyProperties(dto, wmNews);
        if (dto.getImages() != null && dto.getImages().size() > 0) {
            wmNews.setImages(StringUtils.join(dto.getImages(), ","));
        }
        if (dto.getType().equals(WemediaConstants.WM_NEWS_TYPE_AUTO)) {
            wmNews.setType(null);
        }

        saveOrUpdateWmNews(wmNews, userId);
        //草稿 直接返回 不保存文章和素材的关系
        if (dto.getStatus().equals(WmNews.Status.NORMAL.getCode())) {
            return ResponseResult.okResult(HttpCodeEnum.SUCCESS);
        }

        //获取文章内容中的图片
        List<String> materials = getUrlInfo(dto.getContent());
        saveRelativeInfoForContent(materials, wmNews.getId());
        saveRelativeInfoForCover(dto, wmNews, materials);
        // 异步审核文章
        wmNewsAutoScanService.autoScanWmNews(wmNews.getId());
        return ResponseResult.okResult(HttpCodeEnum.SUCCESS);
    }

    private void saveRelativeInfoForCover(WmNewsDto dto, WmNews wmNews, List<String> materials) {
        List<String> images = dto.getImages();
        if (WemediaConstants.WM_NEWS_TYPE_AUTO.equals(dto.getType())) {
            if (materials.size() > 3) {
                wmNews.setType(WemediaConstants.WM_NEWS_MANY_IMAGE);
                images = materials.subList(0, 3);
            } else if(materials.size() == 0) {
                wmNews.setType(WemediaConstants.WM_NEWS_NONE_IMAGE);
            } else {
                wmNews.setType(WemediaConstants.WM_NEWS_SINGLE_IMAGE);
                images = materials.subList(0, 1);
            }
            if (images != null && images.size() > 0) {
                wmNews.setImages(StringUtils.join(images, ","));
            }
            updateById(wmNews);
        }
        if (images != null && images.size() > 0) {
            saveRelativeInfo(images, wmNews.getId(), WemediaConstants.WM_COVER_REFERENCE);
        }
    }

    private void saveRelativeInfoForContent(List<String> materials, Integer NewsId) {
        saveRelativeInfo(materials, NewsId, WemediaConstants.WM_CONTENT_REFERENCE);
    }

    private void saveRelativeInfo(List<String> materials, Integer NewsId, Short type) {
        if (materials == null || materials.size() == 0) {
            return;
        }
        List<WmMaterial> wmMaterials = wmMaterialMapper.selectList(Wrappers.<WmMaterial>lambdaQuery().in(WmMaterial::getUrl, materials));
        if (wmMaterials == null || wmMaterials.size() == 0 || materials.size() != wmMaterials.size()) {
            throw new CustomException(HttpCodeEnum.MATERIAL_REFERENCE_FAIL);
        }
        List<Integer> idList = wmMaterials.stream().map(WmMaterial::getId).collect(Collectors.toList());
        wmNewsMaterialMapper.saveRelations(idList, NewsId, type);
    }


    private List<String> getUrlInfo(String content) {
        List<Map> maps = JSON.parseArray(content, Map.class);
        List<String> materials = new ArrayList<>();
        for (Map map : maps) {
            if ("image".equals(map.get("type"))) {
                materials.add((String) map.get("value"));
            }
        }
        return materials;
    }

    private void saveOrUpdateWmNews(WmNews wmNews, Integer userId) {
        wmNews.setUserId(userId);
        wmNews.setCreatedTime(new Date());
        wmNews.setSubmitedTime(new Date());
        wmNews.setEnable((short) 1);
        if (wmNews.getId() == null) {
            save(wmNews);
        } else {
            wmNewsMaterialMapper.delete(Wrappers.<WmNewsMaterial>lambdaQuery().eq(WmNewsMaterial::getNewsId, wmNews.getId()));
            updateById(wmNews);
        }
    }

}
