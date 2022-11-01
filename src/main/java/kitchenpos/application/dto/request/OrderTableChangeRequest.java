package kitchenpos.application.dto.request;

public class OrderTableChangeRequest {

    private Long id;
    private int numberOfGuests;
    private boolean empty;

    private OrderTableChangeRequest() {
    }

    public OrderTableChangeRequest(final Long id, final int numberOfGuests, final boolean empty) {
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
