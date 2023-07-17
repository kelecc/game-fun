package top.kelecc.weMedia.controller.v1;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.kelecc.model.common.dtos.ResponseResult;
import top.kelecc.model.weMedia.pojo.WmChannel;
import top.kelecc.weMedia.service.WmChannelService;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 可乐
 * @version 1.0
 * @description:
 * @date 2023/7/17 20:23
 */
@RestController
@RequestMapping("/api/v1/channel")
@Api(value = "频道模块", tags = "频道模块")
public class WmChannelController {
    @Resource
    private WmChannelService wmChannelService;

    @GetMapping("/channels")
    @ApiOperation("查询所有频道")
    public ResponseResult findAll(){
        List<WmChannel> channels = wmChannelService.list(new LambdaQueryWrapper<WmChannel>().eq(WmChannel::getStatus, 1));
        return ResponseResult.okResult(channels);
    }
}
