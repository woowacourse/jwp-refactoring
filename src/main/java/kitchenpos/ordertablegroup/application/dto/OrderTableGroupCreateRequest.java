package kitchenpos.ordertablegroup.application.dto;

import kitchenpos.order.application.dto.OrderTableId;

import java.util.List;
import java.util.stream.Collectors;

public class OrderTableGroupCreateRequest {

    private List<OrderTableId> orderTables;

    private OrderTableGroupCreateRequest() {
    }

    public OrderTableGroupCreateRequest(final List<OrderTableId> orderTables) {
        this.orderTables = orderTables;
    }

    public List<Long> extractIds() {
        return orderTables.stream()
                .map(OrderTableId::getId)
                .collect(Collectors.toList());
    }

    public List<OrderTableId> getOrderTables() {
        return orderTables;
    }
}
