package kitchenpos.ui.dto;

import kitchenpos.application.dto.UpdateGuestNumberDto;

public class TableGuestNumberRequestDto {

    private Integer numberOfGuests;

    public TableGuestNumberRequestDto() {
    }

    public UpdateGuestNumberDto toUpdateGuestNumberDto(Long orderTableId) {
        return new UpdateGuestNumberDto(orderTableId, numberOfGuests);
    }

    public void setNumberOfGuests(Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }
}
