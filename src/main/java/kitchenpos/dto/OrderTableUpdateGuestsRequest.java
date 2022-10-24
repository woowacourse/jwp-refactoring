package kitchenpos.dto;

public class OrderTableUpdateGuestsRequest {

    private final Long numberOfGuests;

    public OrderTableUpdateGuestsRequest(Long numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public Long getNumberOfGuests() {
        return numberOfGuests;
    }
}
