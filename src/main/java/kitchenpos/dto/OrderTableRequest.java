package kitchenpos.dto;

import kitchenpos.domain.OrderTable;

public class OrderTableRequest {

    private Long id;

    private OrderTableRequest() {
    }

    public OrderTable toOrderTable() {
        final OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        return orderTable;
    }

    public Long getId() {
        return id;
    }
}
