package kitchenpos.dto.response;

public class OrderTableCreateResponse {

    private Long id;
    private int numberOfGuests;
    private boolean empty;

    private OrderTableCreateResponse() {
    }

    public OrderTableCreateResponse(final Long id, final int numberOfGuests, final boolean empty) {
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
