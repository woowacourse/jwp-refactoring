package kitchenpos.application.dto.response;

import kitchenpos.domain.table.OrderTable;

public class OrderTableResponse {

    private final Long id;
    private final int numberOfGuests;
    private final boolean empty;

    private OrderTableResponse() {
        this(null, 0, false);
    }

    public OrderTableResponse(Long id, int numberOfGuests, boolean empty) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableResponse from(OrderTable orderTable) {
        return new OrderTableResponse(
                orderTable.getId(),
                orderTable.getNumberOfGuests().getCount(),
                orderTable.isEmpty()
        );
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
