package kitchenpos.table.application.dto;

import kitchenpos.table.domain.OrderTable;

public class OrderTableRequest {

    private Long id;

    public OrderTableRequest() {
    }

    public OrderTableRequest(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public OrderTable toOrderTable() {
        return new OrderTable(id);
    }
}
