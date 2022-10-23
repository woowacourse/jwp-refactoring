package kitchenpos.ui.dto;

import kitchenpos.application.dto.EmptyTableDto;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class EmptyTableRequestDto {

    private Boolean empty;

    public EmptyTableDto toEmptyTableDto(Long orderTableId) {
        return new EmptyTableDto(orderTableId, empty);
    }
}
