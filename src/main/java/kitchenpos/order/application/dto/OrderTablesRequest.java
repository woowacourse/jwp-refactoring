package kitchenpos.order.application.dto;

import java.util.List;

public class OrderTablesRequest {

    private List<Long> orderTableIds;

    private OrderTablesRequest() {
    }

    public OrderTablesRequest(final List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
