package kitchenpos.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.domain.Order;

public class OrderResponse {

    private Long id;
    private OrderTableResponse orderTable;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemResponse> orderLineItems;

    public OrderResponse(Order order) {
        id = order.getId();
        orderTable = new OrderTableResponse(order.getOrderTable());
        orderStatus = order.getOrderStatus();
        orderedTime = order.getOrderedTime();
        orderLineItems = order.getOrderLineItems().stream()
            .map(it -> new OrderLineItemResponse(it))
            .collect(Collectors.toList());
    }

}
