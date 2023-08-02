package top.kelecc.model.weMedia.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author 可乐
 * @version 1.0
 * @description:
 * @date 2023/8/2 14:57
 */
@Data
public class WmNewsUpOrDownDto {
    @NotNull(message = "文章id不能为空")
    private Integer id;
    /**
     * 是否上架  0 下架  1 上架
     */
    @NotNull(message = "上下架参数不能为空")
    private Short enable;
}
