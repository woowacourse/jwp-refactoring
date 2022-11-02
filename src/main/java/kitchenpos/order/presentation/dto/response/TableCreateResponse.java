package kitchenpos.order.presentation.dto.response;

import kitchenpos.order.domain.OrderTable;

public class TableCreateResponse {

    private Long id;
    private int numberOfGuests;
    private boolean empty;

    private TableCreateResponse() {
    }

    public TableCreateResponse(OrderTable table) {
        id = table.getId();
        numberOfGuests = table.getNumberOfGuests();
        empty = table.isEmpty();
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
