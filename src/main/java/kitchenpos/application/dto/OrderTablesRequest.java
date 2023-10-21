package kitchenpos.application.dto;

import java.util.List;

public class OrderTablesRequest {

    private final List<Long> orderTableIds;

    public OrderTablesRequest(final List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
