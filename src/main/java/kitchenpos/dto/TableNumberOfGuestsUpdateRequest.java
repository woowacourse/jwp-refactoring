package kitchenpos.dto;

public class TableNumberOfGuestsUpdateRequest {

    private final Integer numberOfGuests;

    public TableNumberOfGuestsUpdateRequest(Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }
}
