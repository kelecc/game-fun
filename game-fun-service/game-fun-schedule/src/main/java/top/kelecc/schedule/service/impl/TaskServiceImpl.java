package top.kelecc.schedule.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.kelecc.common.constants.ScheduleConstants;
import top.kelecc.common.redis.RedisCache;
import top.kelecc.model.schedule.dto.Task;
import top.kelecc.model.schedule.pojo.Taskinfo;
import top.kelecc.model.schedule.pojo.TaskinfoLogs;
import top.kelecc.schedule.mapper.TaskinfoLogsMapper;
import top.kelecc.schedule.mapper.TaskinfoMapper;
import top.kelecc.schedule.service.TaskService;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

/**
 * @author 可乐
 * @version 1.0
 * @description:
 * @date 2023/7/31 13:10
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class TaskServiceImpl implements TaskService {
    @Resource
    private TaskinfoLogsMapper taskinfoLogsMapper;
    @Resource
    private TaskinfoMapper taskinfoMapper;
    @Resource
    private RedisCache redisCache;
    @Resource
    private RedissonClient redissonClient;
    private final String REFRESH_LOCK_KEY = "refreshTaskLock";
    private final String POLL_LOCK_KEY = "pollTaskLock";

    @Override
    public long addTask(Task task) {
        // 1. 添加到数据库
        try {
            addTaskToDb(task);
        } catch (Exception e) {
            log.error("添加任务失败", e);
            throw new RuntimeException(e);
        }

        // 2. 添加到redis
        addTaskToRedis(task);
        return task.getTaskId();
    }

    @Override
    public boolean cancelTask(long taskId) {
        Task task = null;
        try {
            task = updateDb(taskId, ScheduleConstants.CANCELLED);
        } catch (Exception e) {
            log.error("取消任务失败", e);
            throw new RuntimeException(e);
        }
        removeTaskFromRedis(task);
        return true;
    }

    @Override
    public Task poll(int type, int priority) {
        RLock lock = redissonClient.getLock(POLL_LOCK_KEY);
        lock.lock();
        Task task = null;
        try {
            String key = type + "_" + priority;
            task = redisCache.getCacheListLeftPop(ScheduleConstants.TOPIC + key);
            if (task != null) {
                updateDb(task.getTaskId(), ScheduleConstants.EXECUTED);
            }
        } catch (Exception e) {
            log.error("获取任务失败", e);
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
        return task;
    }

    @Scheduled(cron = "0 */1 * * * ?")
    public void refresh() {
        RLock lock = redissonClient.getLock(REFRESH_LOCK_KEY);
        lock.lock();
        try {
            log.info("{}开始刷新任务队列", new Date(System.currentTimeMillis()));
            // 1.获取未来任务的集合的key
            redisCache.scan(ScheduleConstants.FUTURE + "*").stream().forEach(key -> {
                String topicKey = ScheduleConstants.TOPIC + key.split(ScheduleConstants.FUTURE)[1];
                // 2.获取未来任务的集合
                Set<String> taskSet = redisCache.zRangeByScore(key, 0, System.currentTimeMillis());
                // 3.将未来任务的集合添加到任务队列
                if (taskSet != null && taskSet.size() > 0) {
                    redisCache.refreshWithPipeline(key, topicKey, taskSet);
                    log.info("成功的将{}下待执行的{}条任务数据刷新到{}下", key, taskSet.size(), topicKey);
                }
            });
        } finally {
            lock.unlock();
        }
    }

    @Scheduled(cron = "30 */5 * * * ?")
    @PostConstruct
    public void reloadData() {
        RLock refreshLock = redissonClient.getLock(REFRESH_LOCK_KEY);
        RLock pollLock = redissonClient.getLock(POLL_LOCK_KEY);
        refreshLock.lock();
        pollLock.lock();
        try {
            clearCache();
            log.info("开始将数据库中的任务同步到缓存中");
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MINUTE, 5);
            taskinfoMapper.selectList(Wrappers.<Taskinfo>lambdaQuery().lt(Taskinfo::getExecuteTime, calendar.getTime()))
                    .stream()
                    .forEach(taskinfo -> {
                        Task task = new Task();
                        BeanUtils.copyProperties(taskinfo, task);
                        task.setExecuteTime(taskinfo.getExecuteTime().getTime());
                        addTaskToRedis(task);
                    });
        } finally {
            refreshLock.unlock();
            pollLock.unlock();
        }
    }

    private void clearCache() {
        Set<String> topicKeys = redisCache.scan(ScheduleConstants.TOPIC + "*");
        Set<String> futureKeys = redisCache.scan(ScheduleConstants.FUTURE + "*");
        redisCache.deleteObject(topicKeys);
        redisCache.deleteObject(futureKeys);
    }

    private void removeTaskFromRedis(Task task) {
        String key = task.getTaskType() + "_" + task.getPriority();
        if (task.getExecuteTime() <= System.currentTimeMillis()) {
            redisCache.deleteFromList(ScheduleConstants.TOPIC + key, 0, task);
        } else {
            redisCache.deleteFromZset(ScheduleConstants.FUTURE + key, task);
        }
    }

    private Task updateDb(long taskId, int status) {
        taskinfoMapper.deleteById(taskId);

        TaskinfoLogs taskinfoLogs = taskinfoLogsMapper.selectById(taskId);
        taskinfoLogs.setStatus(status);
        taskinfoLogsMapper.updateById(taskinfoLogs);
        Task task = new Task();
        BeanUtils.copyProperties(taskinfoLogs, task);
        task.setExecuteTime(taskinfoLogs.getExecuteTime().getTime());
        return task;
    }

    private void addTaskToRedis(Task task) {
        String key = task.getTaskType() + "_" + task.getPriority();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 5);
        long nextScheduleTime = calendar.getTimeInMillis();

        if (task.getExecuteTime() <= System.currentTimeMillis()) {
            redisCache.setCacheListRightPush(ScheduleConstants.TOPIC + key, task);
        } else if (task.getExecuteTime() <= nextScheduleTime) {
            redisCache.setCacheZset(ScheduleConstants.FUTURE + key, task, task.getExecuteTime());
        }
    }

    private void addTaskToDb(Task task) {
        try {
            // 保存到taskinfo表
            Taskinfo taskinfo = new Taskinfo();
            BeanUtils.copyProperties(task, taskinfo);
            taskinfo.setExecuteTime(new Date(task.getExecuteTime()));
            taskinfoMapper.insert(taskinfo);

            task.setTaskId(taskinfo.getTaskId());
            // 保存到taskinfo_logs表
            TaskinfoLogs taskinfoLogs = new TaskinfoLogs();
            BeanUtils.copyProperties(task, taskinfoLogs);
            taskinfoLogs.setVersion(1);
            taskinfoLogs.setTaskType(ScheduleConstants.SCHEDULED);
            taskinfoLogs.setExecuteTime(new Date(task.getExecuteTime()));
            taskinfoLogsMapper.insert(taskinfoLogs);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
