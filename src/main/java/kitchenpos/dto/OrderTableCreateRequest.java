package kitchenpos.dto;

import kitchenpos.domain.OrderTable;

public class OrderTableCreateRequest {
    private Long id;
    private int numberOfGuests;
    private boolean empty;

    public OrderTableCreateRequest() {
    }

    public OrderTableCreateRequest(Long id, int numberOfGuests, boolean empty) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable toOrderTable() {
        return new OrderTable(this.numberOfGuests, this.empty);
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
