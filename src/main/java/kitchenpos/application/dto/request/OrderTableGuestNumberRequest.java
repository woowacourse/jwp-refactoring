package kitchenpos.application.dto.request;

public class OrderTableGuestNumberRequest {
    private Integer numberOfGuests;

    public OrderTableGuestNumberRequest() {
    }

    public OrderTableGuestNumberRequest(final Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }
}
