package top.kelecc.weMedia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import top.kelecc.file.starter.service.FileStorageService;
import top.kelecc.model.common.dtos.PageResponseResult;
import top.kelecc.model.common.dtos.ResponseResult;
import top.kelecc.model.common.enums.HttpCodeEnum;
import top.kelecc.model.weMedia.dto.WmMaterialDto;
import top.kelecc.model.weMedia.pojo.WmMaterial;
import top.kelecc.weMedia.mapper.WmMaterialMapper;
import top.kelecc.weMedia.service.WmMaterialService;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;


@Slf4j
@Service
@Transactional
public class WmMaterialServiceImpl extends ServiceImpl<WmMaterialMapper, WmMaterial> implements WmMaterialService {
    @Resource
    private FileStorageService fileStorageService;
    @Resource
    private WmMaterialMapper wmMaterialMapper;

    @Override
    public ResponseResult uploadPicture(MultipartFile multipartFile, Integer userId) {
        // 1. 上传图片到minio
        String fileName = UUID.randomUUID().toString().replace("-", "");
        String originalFilename = multipartFile.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String url = null;
        try {
            url = fileStorageService.uploadImgFile("", fileName + suffix, multipartFile.getInputStream());
            log.info("上传图片到minio成功，url:{}", url);
        } catch (IOException e) {
            log.error("上传图片到minio失败，e:{}", e.getMessage());
            return ResponseResult.errorResult(HttpCodeEnum.SERVER_ERROR, "上传图片到minio失败");
        }
        // 2. 保存素材信息到数据库
        WmMaterial wmMaterial = new WmMaterial();
        wmMaterial.setUrl(url);
        wmMaterial.setUserId(userId);
        wmMaterial.setType((short) 0);
        wmMaterial.setIsCollection((short) 0);
        wmMaterial.setCreatedTime(new Date());
        int result = wmMaterialMapper.insert(wmMaterial);
        if (result <= 0) {
            return ResponseResult.errorResult(HttpCodeEnum.SERVER_ERROR, "保存素材信息到数据库失败");
        }
        return ResponseResult.okResult(wmMaterial);
    }

    @Override
    public ResponseResult findList(Integer userId, WmMaterialDto dto) {
        //分页查询
        IPage<WmMaterial> page = new Page<>(dto.getPage(), dto.getSize());
        LambdaQueryWrapper<WmMaterial> wrapper = new LambdaQueryWrapper<>();
        //是否只查收藏的
        if (dto.getIsCollection() == 1) {
            wrapper.eq(WmMaterial::getIsCollection, dto.getIsCollection());
        }
        //按用户id查
        wrapper.eq(WmMaterial::getUserId, userId);
        wrapper.orderByDesc(WmMaterial::getCreatedTime);
        page = page(page, wrapper);
        PageResponseResult pageResponseResult = new PageResponseResult(dto.getPage(), dto.getSize(), (int) page.getTotal());
        pageResponseResult.setData(page.getRecords());
        return pageResponseResult;
    }
}
