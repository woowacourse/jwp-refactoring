package kitchenpos.dto.table;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderTable;

public class OrderTablesResponse {

    private final List<OrderTableResponse> orderTables;

    private OrderTablesResponse(final List<OrderTableResponse> orderTables) {
        this.orderTables = orderTables;
    }

    public static OrderTablesResponse from(final List<OrderTable> orderTables) {
        List<OrderTableResponse> orderTableResponses = orderTables.stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toUnmodifiableList());
        return new OrderTablesResponse(orderTableResponses);
    }

    public List<OrderTableResponse> getOrderTables() {
        return orderTables;
    }
}
