package top.kelecc.weMedia.controller.v1;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.kelecc.model.common.dtos.ResponseResult;
import top.kelecc.model.common.enums.HttpCodeEnum;
import top.kelecc.model.weMedia.dto.WmNewsDto;
import top.kelecc.model.weMedia.dto.WmNewsPageReqDto;
import top.kelecc.model.weMedia.dto.WmNewsUpOrDownDto;
import top.kelecc.security.constants.SecurityMapKeyConstants;
import top.kelecc.weMedia.service.WmNewsService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author 可乐
 * @version 1.0
 * @description:
 * @date 2023/7/17 21:33
 */
@RestController
@RequestMapping("/api/v1/news")
@Api(value = "内容模块", tags = "内容模块")
public class WmNewsController {
    @Resource
    private WmNewsService wmNewsService;

    @PostMapping("/list")
    @ApiOperation("查询文章列表")
    public ResponseResult findAll(HttpServletRequest request, @RequestBody WmNewsPageReqDto dto){
        if (dto == null){
            return ResponseResult.errorResult(HttpCodeEnum.PARAM_INVALID);
        }
        dto.checkParam();
        Integer userId = (Integer) request.getAttribute(SecurityMapKeyConstants.ID_KEY);
        return wmNewsService.findByWmNewsPageReqDto(dto, userId);
    }

    @PostMapping("/submit")
    @ApiOperation("添加文章")
    public ResponseResult saveNews(HttpServletRequest request, @RequestBody @Validated WmNewsDto dto){
        Integer userId = (Integer) request.getAttribute(SecurityMapKeyConstants.ID_KEY);
        return wmNewsService.submitNews(dto, userId);
    }

    @PostMapping("/down_or_up")
    public ResponseResult downOrUp(@RequestBody @Validated WmNewsUpOrDownDto dto){
        if (dto.getEnable() != 0 && dto.getEnable() != 1){
            return ResponseResult.errorResult(HttpCodeEnum.PARAM_INVALID, "上下架参数不合法");
        }
        return wmNewsService.downOrUp(dto);
    }
}
