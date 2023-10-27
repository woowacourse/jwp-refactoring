package kitchenpos.ordertable.ui.dto;

public class ChangeOrderTableNumberOfGuestsRequest {

    private final Integer numberOfGuests;

    public ChangeOrderTableNumberOfGuestsRequest(final Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }
}
