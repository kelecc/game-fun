package top.kelecc.model.weMedia.dto;

import lombok.Data;
import top.kelecc.model.common.dtos.PageRequestDto;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

@Data
public class WmMaterialDto extends PageRequestDto {

    /**
     * 1 收藏
     * 0 未收藏
     */
    @NotNull(message = "收藏状态不能为空")
    @Digits(integer = 1, fraction = 0, message = "收藏状态只能为0或1")
    private Short isCollection;
}
