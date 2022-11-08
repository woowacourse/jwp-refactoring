package kitchenpos.table.application.dto.request;

import kitchenpos.table.domain.OrderTable;

public class OrderTableIdRequest {

    private final Long id;

    public OrderTableIdRequest(final Long id) {
        this.id = id;
    }

    public OrderTable toEntity() {
        return new OrderTable(id);
    }
}
