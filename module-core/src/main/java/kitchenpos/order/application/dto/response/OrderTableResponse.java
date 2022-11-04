package kitchenpos.order.application.dto.response;

public class OrderTableResponse {

    private final Long id;
    private final int numberOfGuests;
    private final boolean empty;

    public OrderTableResponse(final Long id, final int numberOfGuests, final boolean empty) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Long getId() {
        return id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
