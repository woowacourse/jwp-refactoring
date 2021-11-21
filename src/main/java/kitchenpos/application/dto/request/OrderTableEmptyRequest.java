package kitchenpos.application.dto.request;

import kitchenpos.domain.OrderTable;

public class OrderTableEmptyRequest {

    private boolean empty;

    public OrderTableEmptyRequest() {
    }

    public OrderTableEmptyRequest(boolean empty) {
        this.empty = empty;
    }

    public OrderTable toEntity() {
        return new OrderTable.OrderTableBuilder()
                .setEmpty(empty)
                .build();
    }

    public boolean isEmpty() {
        return empty;
    }
}
