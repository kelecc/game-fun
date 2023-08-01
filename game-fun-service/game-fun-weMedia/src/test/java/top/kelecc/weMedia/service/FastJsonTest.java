package top.kelecc.weMedia.service;

import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.kelecc.api.schedule.IScheduleClient;
import top.kelecc.model.common.dtos.ResponseResult;
import top.kelecc.model.schedule.dto.Task;

import javax.annotation.Resource;
import java.util.Base64;
import java.util.Map;

/**
 * @author 可乐
 * @version 1.0
 * @description:
 * @date 2023/8/1 16:27
 */
@SpringBootTest
public class FastJsonTest {
    @Resource
    private IScheduleClient scheduleClient;

    @Test
    void addTask() {
        Task task = new Task();
        task.setExecuteTime(System.currentTimeMillis());
        task.setTaskType(888);
        task.setPriority(888);
        task.setParameters(JSON.toJSONBytes(10086));
        scheduleClient.addTask(task);
    }

    @Test
    void poll() {
        ResponseResult task = scheduleClient.poll(888, 888);
        Map map = (Map) task.getData();
        String o = (String) map.get("parameters");
        byte[] decode = Base64.getDecoder().decode(o);
        Object o1 = JSON.parseObject(decode, Integer.class);
        System.out.println(o1);
    }

}
