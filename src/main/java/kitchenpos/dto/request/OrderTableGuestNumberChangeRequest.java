package kitchenpos.dto.request;

public class OrderTableGuestNumberChangeRequest {

    private Integer numberOfGuests;

    private OrderTableGuestNumberChangeRequest() {
    }

    public OrderTableGuestNumberChangeRequest(Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }
}
