package kitchenpos.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderTable;

public class OrderTablesResponse {

    private List<OrderTableResponse> orderTableResponses;

    private OrderTablesResponse() {
    }

    private OrderTablesResponse(final List<OrderTableResponse> orderTableResponses) {
        this.orderTableResponses = orderTableResponses;
    }

    public static OrderTablesResponse of(List<OrderTable> orderTables) {
        List<OrderTableResponse> orderTableResponses = orderTables.stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList());

        return new OrderTablesResponse(orderTableResponses);
    }

    public List<OrderTableResponse> getOrderTableResponses() {
        return orderTableResponses;
    }
}
