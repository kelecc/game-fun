package top.kelecc.schedule.service.impl;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.kelecc.common.redis.RedisCache;
import top.kelecc.model.schedule.dto.Task;
import top.kelecc.schedule.service.TaskService;

import javax.annotation.Resource;
import java.util.Collection;

@SpringBootTest
public class TaskServiceImplTest {
    @Resource
    private TaskService taskService;
    @Resource
    private RedisCache redisCache;
    @Test
    public void addTask() {
        Task task = new Task();
        task.setTaskType(0);
        task.setPriority(1);
        long l = 0;
        for (int i = 0; i < 5; i++) {
            task.setExecuteTime(System.currentTimeMillis() + 4*60*1000);
            taskService.addTask(task);
            task.setTaskId(null);
        }
    }

    @Test
    void cancelTask() {
        boolean b = taskService.cancelTask(1685928310032834561L);
        Assertions.assertTrue(b);
    }

    @Test
    void pollTest() {
        taskService.poll(0, 1);
        taskService.poll(0, 1);
        taskService.poll(0, 1);
        taskService.poll(0, 1);
        taskService.poll(0, 1);
        taskService.poll(0, 1);
        taskService.poll(0, 1);
        taskService.poll(0, 1);
    }

    @Test
    void keysTest() {
        Collection<String> keys = redisCache.scan("topic_*");
        keys.stream().forEach(System.out::println);
    }
}
