package kitchenpos.dto;

public class OrderTableCreateRequest {

    private final Integer numberOfGuests;
    private final boolean empty;

    public OrderTableCreateRequest(Integer numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean getEmpty() {
        return empty;
    }
}
