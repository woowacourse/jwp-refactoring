package kitchenpos.table.dto;

public class ChangeNumberOfGuestsRequest {

    private int numberOfGuests;

    public ChangeNumberOfGuestsRequest() {
    }

    public ChangeNumberOfGuestsRequest(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public ChangeNumberOfGuestsDto toChangeNumberOfGuestsDto() {
        return new ChangeNumberOfGuestsDto(numberOfGuests);
    }
}
