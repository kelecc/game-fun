package top.kelecc.schedule.feign;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.kelecc.api.schedule.IScheduleClient;
import top.kelecc.model.common.dtos.ResponseResult;
import top.kelecc.model.common.enums.HttpCodeEnum;
import top.kelecc.model.schedule.dto.Task;
import top.kelecc.schedule.service.TaskService;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * @author 可乐
 * @version 1.0
 * @description:
 * @date 2023/7/31 13:20
 */
@RestController
public class ScheduleClient implements IScheduleClient {
    @Resource
    private TaskService taskService;

    @Override
    @PostMapping("/api/v1/task/add")
    public ResponseResult addTask(@Validated @RequestBody Task task) {
        Long taskId = null;
        try {
            taskId = taskService.addTask(task);
        } catch (Exception e) {
            return ResponseResult.errorResult(HttpCodeEnum.SERVER_ERROR, "添加任务失败");
        }
        return ResponseResult.okResult(new HashMap<String, Long>(1).put("taskId", taskId));
    }

    @Override
    @GetMapping("/api/v1/task/cancel/{taskId}")
    public ResponseResult cancelTask(@PathVariable("taskId") long taskId) {
        try {
            taskService.cancelTask(taskId);
        } catch (Exception e) {
            return ResponseResult.errorResult(HttpCodeEnum.SERVER_ERROR, "取消任务失败");
        }
        return ResponseResult.okResult("取消任务成功");
    }

    @Override
    @GetMapping("/api/v1/task/poll/{type}/{priority}")
    public ResponseResult poll(@PathVariable("type") int type, @PathVariable("priority") int priority) {
        Task task = null;
        try {
            task = taskService.poll(type, priority);
        } catch (Exception e) {
            return ResponseResult.errorResult(HttpCodeEnum.SERVER_ERROR, "拉取任务失败");
        }
        return ResponseResult.okResult(task);
    }
}
