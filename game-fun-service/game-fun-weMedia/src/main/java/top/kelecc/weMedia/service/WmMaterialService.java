package top.kelecc.weMedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;
import top.kelecc.model.common.dtos.ResponseResult;
import top.kelecc.model.weMedia.dto.WmMaterialDto;
import top.kelecc.model.weMedia.pojo.WmMaterial;

public interface WmMaterialService extends IService<WmMaterial> {
    /**
     * 图片上传
     *
     * @param multipartFile
     * @return
     */
    ResponseResult uploadPicture(MultipartFile multipartFile, Integer userId);

    ResponseResult findList(Integer userId, WmMaterialDto dto);
}
