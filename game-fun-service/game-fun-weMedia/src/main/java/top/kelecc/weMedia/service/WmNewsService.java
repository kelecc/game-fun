package top.kelecc.weMedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.kelecc.model.common.dtos.ResponseResult;
import top.kelecc.model.weMedia.dto.WmNewsDto;
import top.kelecc.model.weMedia.dto.WmNewsPageReqDto;
import top.kelecc.model.weMedia.pojo.WmNews;

public interface WmNewsService extends IService<WmNews> {

    /**
     * 查询文章
     *
     * @param dto
     * @param userId
     * @return
     */
    ResponseResult findByWmNewsPageReqDto(WmNewsPageReqDto dto, Integer userId);

    /**
     * 发布文章或保存草稿
     *
     * @param dto
     * @return
     */
    ResponseResult submitNews(WmNewsDto dto, Integer userId);
}
