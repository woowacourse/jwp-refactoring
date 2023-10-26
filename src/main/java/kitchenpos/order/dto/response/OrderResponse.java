package kitchenpos.order.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.Order;

public class OrderResponse {

    private final Long id;
    private final Long OrderTableId;
    private final LocalDateTime orderedTime;
    private final List<OrderLineItemResponse> orderLineItems;
    private final String orderStatus;

    private OrderResponse(final Long id, final Long orderTableId, final LocalDateTime orderedTime,
                          final List<OrderLineItemResponse> orderLineItems,
                          final String orderStatus) {
        this.id = id;
        OrderTableId = orderTableId;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
        this.orderStatus = orderStatus;
    }

    public static OrderResponse of(final Order order) {
        return new OrderResponse(
                order.getId(),
                order.getOrderTable().getId(),
                order.getOrderedTime(),
                order.getOrderLineItems().stream()
                        .map(OrderLineItemResponse::of)
                        .collect(Collectors.toUnmodifiableList()),
                order.getOrderStatus().name()
        );
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return OrderTableId;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItemResponse> getOrderLineItems() {
        return orderLineItems;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
