package kitchenpos.order.ui.dto;

import javax.validation.constraints.NotNull;

public class TableChangeGuestNumberRequest {

    @NotNull
    private Integer numberOfGuests;

    protected TableChangeGuestNumberRequest() {
    }

    public TableChangeGuestNumberRequest(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }
}
