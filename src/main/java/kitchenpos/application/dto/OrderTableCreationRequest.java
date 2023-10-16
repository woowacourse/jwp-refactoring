package kitchenpos.application.dto;

public class OrderTableCreationRequest {
    private final Integer numberOfGuests;
    private final boolean empty;

    public OrderTableCreationRequest(final Integer numberOfGuests, final boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
