package kitchenpos.dto;

public class OrderTableNumberOfGuestsChangeRequest {
    private Integer numberOfGuests;

    protected OrderTableNumberOfGuestsChangeRequest() {
    }

    public OrderTableNumberOfGuestsChangeRequest(final Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }
}
