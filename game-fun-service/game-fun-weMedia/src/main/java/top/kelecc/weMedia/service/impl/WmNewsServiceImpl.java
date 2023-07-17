package top.kelecc.weMedia.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.kelecc.model.weMedia.pojo.WmNews;
import top.kelecc.weMedia.mapper.WmNewsMapper;
import top.kelecc.weMedia.service.WmNewsService;

@Service
@Slf4j
@Transactional
public class WmNewsServiceImpl  extends ServiceImpl<WmNewsMapper, WmNews> implements WmNewsService {


}
