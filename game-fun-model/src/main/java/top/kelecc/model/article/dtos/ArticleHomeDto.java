package top.kelecc.model.article.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author 可乐
 */
@Data
public class ArticleHomeDto {

    /**
     * 最大时间
     */
    @NotNull(message = "maxBehotTime不能为空")
    @ApiModelProperty(value = "列表第一条文章的发布时间", dataType = "Date", example = "1678128000", required = true)
    Date maxBehotTime;
    /**
     * 最小时间
     */
    @ApiModelProperty(value = "列表最下面的文章的发布时间", dataType = "Date", example = "1678128000", required = true)
    @NotNull(message = "minBehotTime不能为空")
    Date minBehotTime;
    /**
     * 分页size
     */
    @NotNull(message = "size不能为空")
    @DecimalMax(value = "50", message = "size不能超过50")
    @ApiModelProperty(value = "分页size,最大50", dataType = "Integer", example = "20", required = true)
    Integer size;
    /**
     * 频道ID
     */
    @ApiModelProperty(value = "频道ID", dataType = "Integer", example = "1", required = true)
    @NotBlank(message = "tag不能为空")
    String tag;
}
