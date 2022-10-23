package kitchenpos.ui.dto;

import kitchenpos.application.dto.UpdateGuestNumberDto;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
public class TableGuestNumberRequestDto {

    private Integer numberOfGuests;

    public UpdateGuestNumberDto toUpdateGuestNumberDto(Long orderTableId) {
        return new UpdateGuestNumberDto(orderTableId, numberOfGuests);
    }
}
