package top.kelecc.weMedia.service.impl;

import com.alibaba.fastjson.JSON;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import top.kelecc.api.schedule.IScheduleClient;
import top.kelecc.common.TaskTypeEnum;
import top.kelecc.model.common.dtos.ResponseResult;
import top.kelecc.model.schedule.dto.Task;
import top.kelecc.weMedia.service.WmNewsTaskService;

import javax.annotation.Resource;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

/**
 * @author 可乐
 * @version 1.0
 * @description:
 * @date 2023/7/31 23:39
 */
@Service
@Slf4j
public class WmNewsTaskServiceImpl implements WmNewsTaskService {
    @Resource
    private IScheduleClient scheduleClient;
    @Resource
    private WmNewsAutoScanServiceImpl wmNewsAutoScanService;

    @Override
    @Async
    @GlobalTransactional
    public void addNewsToTask(Integer id, Date publishTime) {
        log.info("添加文章到延迟队列中，文章id：{}，设定发布时间：{}", id, publishTime);
        Task task = new Task();
        task.setExecuteTime(publishTime.getTime());
        task.setTaskType(TaskTypeEnum.NEWS_SCAN_TIME.getTaskType());
        task.setPriority(TaskTypeEnum.NEWS_SCAN_TIME.getPriority());
        task.setParameters(JSON.toJSONBytes(id));
        scheduleClient.addTask(task);
        log.info("添加文章到延迟队列成功");
    }

    @Override
    @Scheduled(fixedRate = 1000)
    @GlobalTransactional
    public void scanNewsByTask() {
        ResponseResult result = scheduleClient.poll(TaskTypeEnum.NEWS_SCAN_TIME.getTaskType(), TaskTypeEnum.NEWS_SCAN_TIME.getPriority());
        if (result.getCode().equals(200) && result.getData() != null) {
            Map map = (Map) result.getData();
            String str = (String) map.get("parameters");
            byte[] decode = Base64.getDecoder().decode(str);
            Integer id = JSON.parseObject(decode, Integer.class);
            wmNewsAutoScanService.autoScanWmNews(id);
            log.info("定时审核文章id：{}", id);
        }
    }
}
