package top.kelecc.model.schedule.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class Task implements Serializable {

    /**
     * 任务id
     */
    private Long taskId;
    /**
     * 类型
     */
    @NotNull(message = "任务类型不能为空")
    private Integer taskType;

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * 执行id
     */
    @NotNull(message = "executeTime不能为空")
    private long executeTime;

    /**
     * task参数
     */
    private byte[] parameters;

}
