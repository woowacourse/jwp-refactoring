package kitchenpos.ordertable.application.dto.response;

import kitchenpos.ordertable.OrderTable;

public class OrderTableInTableGroupResponse {

    private final Long id;
    private final int numberOfGuests;
    private final boolean empty;

    public OrderTableInTableGroupResponse(final Long id, final int numberOfGuests, final boolean empty) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableInTableGroupResponse from(final OrderTable orderTable) {
        return new OrderTableInTableGroupResponse(
                orderTable.getId(),
                orderTable.getNumberOfGuests().getNumber(),
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
