package kitchenpos.ui.dto;

import kitchenpos.application.dto.UpdateGuestNumberDto;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TableGuestNumberRequestDto {

    private Integer numberOfGuests;

    public UpdateGuestNumberDto toUpdateGuestNumberDto(Long orderTableId) {
        return new UpdateGuestNumberDto(orderTableId, numberOfGuests);
    }
}
