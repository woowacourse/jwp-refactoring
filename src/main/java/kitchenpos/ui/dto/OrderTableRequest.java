package kitchenpos.ui.dto;

import java.util.List;

public class OrderTableRequest {

    private List<OrderTableDto> orderTables;

    private OrderTableRequest() {
    }

    public OrderTableRequest(final List<OrderTableDto> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableDto> getOrderTables() {
        return orderTables;
    }
}
