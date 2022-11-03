package kitchenpos.table.presentation.dto;

import kitchenpos.table.application.dto.UpdateNumberOfGuestRequestDto;

public class UpdateNumberOfGuestRequest {

    private Integer numberOfGuests;

    public UpdateNumberOfGuestRequest(Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public UpdateNumberOfGuestRequestDto toServiceDto(){
        return new UpdateNumberOfGuestRequestDto(numberOfGuests);
    }
}
