package top.kelecc.model.weMedia.dto;

import lombok.Data;
import top.kelecc.model.common.dtos.PageRequestDto;

@Data
public class WmMaterialDto extends PageRequestDto {

    /**
     * 1 收藏
     * 0 未收藏
     */
    private Short isCollection;
}
