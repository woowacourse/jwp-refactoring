package kitchenpos.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.order.OrderTable;

public class OrderTablesResponse {

    private final List<OrderTableResponse> orderTableResponses;

    public OrderTablesResponse(final List<OrderTableResponse> orderTableResponses) {
        this.orderTableResponses = orderTableResponses;
    }

    public List<OrderTableResponse> getOrderTableResponses() {
        return orderTableResponses;
    }

    public static OrderTablesResponse from(final List<OrderTable> orderTables) {
        final List<OrderTableResponse> orderTableResponses = orderTables.stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList());

        return new OrderTablesResponse(orderTableResponses);
    }
}
