package top.kelecc.weMedia.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.kelecc.model.weMedia.pojo.WmMaterial;
import top.kelecc.weMedia.mapper.WmMaterialMapper;
import top.kelecc.weMedia.service.WmMaterialService;


@Slf4j
@Service
@Transactional
public class WmMaterialServiceImpl extends ServiceImpl<WmMaterialMapper, WmMaterial> implements WmMaterialService {


}
