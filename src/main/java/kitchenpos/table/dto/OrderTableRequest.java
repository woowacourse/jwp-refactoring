package kitchenpos.table.dto;

public class OrderTableRequest {

    private Long id;
    private int numberOfGuests;
    private boolean empty;

    public OrderTableRequest() {
    }

    public OrderTableRequest(Long id) {
        this.id = id;
    }

    public OrderTableRequest(boolean empty) {
        this.empty = empty;
    }

    public OrderTableRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public OrderTableRequest(int numberOfGuests, boolean empty) {
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
