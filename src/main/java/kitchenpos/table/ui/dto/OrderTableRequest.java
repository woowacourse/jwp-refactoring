package kitchenpos.table.ui.dto;

import java.util.List;

public class OrderTableRequest {

    private List<OrderTableRequestDto> orderTables;

    private OrderTableRequest() {
    }

    public OrderTableRequest(final List<OrderTableRequestDto> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableRequestDto> getOrderTables() {
        return orderTables;
    }
}
