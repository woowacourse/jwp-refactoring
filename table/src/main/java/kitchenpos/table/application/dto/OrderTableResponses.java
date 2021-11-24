package kitchenpos.table.application.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.domain.OrderTable;

public class OrderTableResponses {
    private final List<OrderTableResponse> orderTableResponses;

    public OrderTableResponses(List<OrderTable> orderTables) {
        this.orderTableResponses = orderTables.stream()
                .map(OrderTableResponse::new)
                .collect(Collectors.toList());
    }

    public List<OrderTableResponse> getOrderTableResponses() {
        return orderTableResponses;
    }
}
