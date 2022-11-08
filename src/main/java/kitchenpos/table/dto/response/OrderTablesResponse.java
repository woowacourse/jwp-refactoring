package kitchenpos.table.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.domain.OrderTable;

public class OrderTablesResponse {

    private List<OrderTableResponse> orderTableResponses;

    private OrderTablesResponse() {
    }

    private OrderTablesResponse(final List<OrderTableResponse> orderTableResponses) {
        this.orderTableResponses = orderTableResponses;
    }

    public static OrderTablesResponse from(List<OrderTable> orderTables) {
        List<OrderTableResponse> orderTableResponses = orderTables.stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());

        return new OrderTablesResponse(orderTableResponses);
    }

    public List<OrderTableResponse> getOrderTableResponses() {
        return orderTableResponses;
    }
}
