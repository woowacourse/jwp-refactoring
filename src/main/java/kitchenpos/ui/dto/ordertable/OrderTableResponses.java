package kitchenpos.ui.dto.ordertable;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.domain.OrderTable;

public class OrderTableResponses {

    private List<OrderTableResponse> orderTableResponses;

    private OrderTableResponses(List<OrderTableResponse> orderTableResponses) {
        this.orderTableResponses = orderTableResponses;
    }

    public static OrderTableResponses from(List<OrderTable> orderTables) {
        return new OrderTableResponses(orderTables.stream()
            .map(OrderTableResponse::from)
            .collect(Collectors.toList()));
    }

    public List<OrderTableResponse> getOrderTableResponses() {
        return orderTableResponses;
    }
}
