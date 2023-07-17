package top.kelecc.weMedia.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.kelecc.model.weMedia.pojo.WmChannel;
import top.kelecc.weMedia.mapper.WmChannelMapper;
import top.kelecc.weMedia.service.WmChannelService;

@Service
@Transactional
@Slf4j
public class WmChannelServiceImpl extends ServiceImpl<WmChannelMapper, WmChannel> implements WmChannelService {


}
