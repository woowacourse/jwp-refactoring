package kitchenpos.ui.dto.ordertable;

import kitchenpos.domain.OrderTable;

import java.util.List;
import java.util.stream.Collectors;

public class OrderTableResponses {

    private List<OrderTableResponse> orderTableResponses;

    public OrderTableResponses(final List<OrderTableResponse> orderTableResponses) {
        this.orderTableResponses = orderTableResponses;
    }

    public static OrderTableResponses from(List<OrderTable> orderTables) {
        return new OrderTableResponses(
                orderTables.stream()
                        .map(OrderTableResponse::from)
                        .collect(Collectors.toList())
        );
    }

    public List<OrderTableResponse> getOrderTableResponses() {
        return orderTableResponses;
    }
}
