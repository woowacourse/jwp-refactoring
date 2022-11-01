package kitchenpos.dto.request;

import kitchenpos.domain.OrderTable;

public class OrderTableRequest {

    private Long id;

    private OrderTableRequest() {
    }

    public OrderTableRequest(final Long id) {
        this.id = id;
    }

    public OrderTable toOrderTable() {
        return new OrderTable(id, null, 0, false);
    }

    public Long getId() {
        return id;
    }
}
