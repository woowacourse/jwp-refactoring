package kitchenpos.ui.dto;

import kitchenpos.application.dto.EmptyTableDto;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
public class EmptyTableRequestDto {

    private Boolean empty;

    public EmptyTableDto toEmptyTableDto(Long orderTableId) {
        return new EmptyTableDto(orderTableId, empty);
    }
}
