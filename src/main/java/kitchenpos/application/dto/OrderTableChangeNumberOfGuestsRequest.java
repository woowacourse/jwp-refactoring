package kitchenpos.application.dto;

public class OrderTableChangeNumberOfGuestsRequest {

    private Integer numberOfGuests;

    public OrderTableChangeNumberOfGuestsRequest(final Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }
}
