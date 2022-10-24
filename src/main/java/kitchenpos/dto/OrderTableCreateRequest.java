package kitchenpos.dto;

public class OrderTableCreateRequest {

    private final Long numberOfGuests;
    private final boolean empty;

    public OrderTableCreateRequest(Long numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Long getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
