package top.kelecc.common.constants;

/**
 * @author 可乐
 * @version 1.0
 * @description:
 * @date 2023/7/31 13:38
 */
public class ScheduleConstants {
    public static final int SCHEDULED = 0;   //初始化状态

    public static final int EXECUTED = 1;       //已执行状态

    public static final int CANCELLED = 2;   //已取消状态

    public static String FUTURE = "future_";   //未来数据key前缀

    public static String TOPIC = "topic_";     //当前数据key前缀
}
