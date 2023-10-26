package kitchenpos.dto.request;

public class OrderTableCreateRequest {

    private final int numberOfGuests;

    public OrderTableCreateRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

}
