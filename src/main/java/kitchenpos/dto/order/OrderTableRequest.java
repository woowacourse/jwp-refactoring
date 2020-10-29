package kitchenpos.dto.order;

import kitchenpos.domain.order.Empty;
import kitchenpos.domain.order.NumberOfGuests;
import kitchenpos.domain.order.OrderTable;

public class OrderTableRequest {
    private Long id;
    private int numberOfGuests;
    private boolean empty;

    private OrderTableRequest() {
    }

    public OrderTableRequest(Long id) {
        this.id = id;
    }

    public OrderTableRequest(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTableRequest(boolean empty) {
        this.empty = empty;
    }

    public OrderTableRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public OrderTable to() {
        return new OrderTable(null, null, NumberOfGuests.of(numberOfGuests), Empty.of(empty));
    }

    public OrderTable forOrderTables() {
        return new OrderTable(id);
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
