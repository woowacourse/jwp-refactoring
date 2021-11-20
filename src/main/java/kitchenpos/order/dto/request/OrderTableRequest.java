package kitchenpos.order.dto.request;

public class OrderTableRequest {

    private Long id;
    private int numberOfGuests;
    private boolean empty;

    public OrderTableRequest() {
    }

    public OrderTableRequest(Long id) {
        this.id = id;
    }

    public OrderTableRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public OrderTableRequest(boolean empty) {
        this.empty = empty;
    }

    public OrderTableRequest(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(final boolean empty) {
        this.empty = empty;
    }
}
