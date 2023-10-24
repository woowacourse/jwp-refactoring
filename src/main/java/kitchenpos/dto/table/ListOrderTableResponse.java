package kitchenpos.dto.table;

import kitchenpos.domain.order.OrderTable;

import java.util.List;
import java.util.stream.Collectors;

public class ListOrderTableResponse {
    private final List<OrderTableResponse> orderTables;

    private ListOrderTableResponse(final List<OrderTableResponse> orderTables) {
        this.orderTables = orderTables;
    }

    public static ListOrderTableResponse of(final List<OrderTable> orderTables) {
        return new ListOrderTableResponse(orderTables.stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList()));
    }

    public List<OrderTableResponse> getOrderTables() {
        return orderTables;
    }
}
