package kitchenpos.ui.dto;

import kitchenpos.domain.OrderTable;

public class CreateTableGroupOrderTableRequest {

    private Long id;

    public CreateTableGroupOrderTableRequest() {
    }

    public CreateTableGroupOrderTableRequest(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public OrderTable toEntity() {
        final OrderTable orderTable = new OrderTable();
        orderTable.setId(id);

        return orderTable;
    }
}
