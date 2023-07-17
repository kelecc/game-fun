package top.kelecc.weMedia.controller.v1;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import top.kelecc.model.common.dtos.ResponseResult;
import top.kelecc.model.common.enums.HttpCodeEnum;
import top.kelecc.model.weMedia.dto.WmMaterialDto;
import top.kelecc.security.constants.SecurityMapKeyConstants;
import top.kelecc.weMedia.service.WmMaterialService;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author 可乐
 * @version 1.0
 * @description:
 * @date 2023/7/17 16:25
 */
@RestController
@RequestMapping("/api/v1/material")
@Api(value = "素材模块", tags = "素材模块")
public class WmMaterialController {
    @Resource
    private WmMaterialService wmMaterialService;


    @PostMapping("/upload_picture")
    @ApiOperation("上传图片素材")
    public ResponseResult uploadPicture(HttpServletRequest request,MultipartFile multipartFile){
        if (multipartFile.isEmpty() || multipartFile == null) {
            return ResponseResult.errorResult(HttpCodeEnum.PARAM_INVALID, "上传文件为空");
        }
        try {
            if (ImageIO.read(multipartFile.getInputStream()) == null) {
                return ResponseResult.errorResult(HttpCodeEnum.PARAM_INVALID, "上传文件不是图片");
            }
        } catch (IOException e) {
            return ResponseResult.errorResult(HttpCodeEnum.PARAM_INVALID, "上传文件不是图片");
        }
        return wmMaterialService.uploadPicture(multipartFile, Integer.valueOf((String) request.getAttribute(SecurityMapKeyConstants.ID_KEY)));
    }

    @PostMapping("/list")
    @ApiOperation("列出所有素材")
    public ResponseResult list(HttpServletRequest request, @Validated @RequestBody WmMaterialDto dto){
        //校验分页参数
        dto.checkParam();
        return wmMaterialService.findList(Integer.valueOf((String)request.getAttribute(SecurityMapKeyConstants.ID_KEY)), dto);
    }
}
