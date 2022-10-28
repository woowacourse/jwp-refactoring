package kitchenpos.dto;

public class OrderTableUpdateGuestsRequest {

    private final Integer numberOfGuests;

    public OrderTableUpdateGuestsRequest(Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }
}
