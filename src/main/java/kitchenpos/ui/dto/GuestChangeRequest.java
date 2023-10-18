package kitchenpos.ui.dto;

import javax.validation.constraints.PositiveOrZero;

public class GuestChangeRequest {

    @PositiveOrZero
    private Integer numberOfGuests;

    public GuestChangeRequest() {
    }

    public GuestChangeRequest(Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }
}
