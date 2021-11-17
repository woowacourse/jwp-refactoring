package kitchenpos.OrderTable.domain.dto.response;

import kitchenpos.OrderTable.domain.OrderTable;

public class OrderTableResponse {

    private Long id;
    private int numberOfGuests;
    private boolean empty;

    private OrderTableResponse(Long id, int numberOfGuests, boolean empty) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    protected OrderTableResponse() {
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

    public static OrderTableResponse toDTO(OrderTable orderTable) {
        return new OrderTableResponse(orderTable.getId(), orderTable.getNumberOfGuests(), orderTable.isEmpty());
    }
}
