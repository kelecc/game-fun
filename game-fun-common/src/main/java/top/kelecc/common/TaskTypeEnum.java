package top.kelecc.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 可乐
 * @version 1.0
 * @description:
 * @date 2023/7/31 23:45
 */
@Getter
@AllArgsConstructor
public enum TaskTypeEnum {

    NEWS_SCAN_TIME(1001, 1, "文章定时审核任务"),
    REMOTEERROR(1002, 2, "第三方接口调用失败，重试任务");
    private final int taskType; //对应具体业务
    private final int priority; //业务不同级别
    private final String desc; //描述信息
}
