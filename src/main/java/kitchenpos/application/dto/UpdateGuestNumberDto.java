package kitchenpos.application.dto;

import lombok.Getter;

@Getter
public class UpdateGuestNumberDto {

    private final Long orderTableId;
    private final Integer numberOfGuests;

    public UpdateGuestNumberDto(Long orderTableId, Integer numberOfGuests) {
        this.orderTableId = orderTableId;
        this.numberOfGuests = numberOfGuests;
    }
}
