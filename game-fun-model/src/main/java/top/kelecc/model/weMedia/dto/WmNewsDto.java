package top.kelecc.model.weMedia.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
public class WmNewsDto {

    private Integer id;
    /**
     * 标题
     */
    @NotBlank(message = "文章标题不能为空")
    private String title;
    /**
     * 频道id
     */
    @NotNull(message = "频道id不能为空")
    private Integer channelId;
    /**
     * 标签
     */
    @NotBlank(message = "文章标签不能为空")
    private String labels;
    /**
     * 发布时间
     */
    @NotNull(message = "发布时间不能为空")
    private Date publishTime;
    /**
     * 文章内容
     */
    @NotBlank(message = "文章内容不能为空")
    private String content;
    /**
     * 文章封面类型  0 无图 1 单图 3 多图 -1 自动
     */
    @NotNull(message = "文章封面类型不能为空")
    @Min(value = -1, message = "文章封面类型不合法")
    @Max(value = 3, message = "文章封面类型不合法")
    private Short type;
    /**
     * 提交时间
     */
    private Date submitedTime;
    /**
     * 状态 提交为1  草稿为0
     */
    @NotNull(message = "文章状态不能为空")
    @Min(value = 0, message = "文章状态不合法")
    @Max(value = 1, message = "文章状态不合法")
    private Short status;

    /**
     * 封面图片列表 多张图以逗号隔开
     */
    private List<String> images;
}
