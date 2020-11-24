package kitchenpos.application.dto;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;

public class OrderCreateRequest {
    private Long orderTableId;
    private List<OrderLineItemCreateRequest> orderLineItems;

    private OrderCreateRequest() {
    }

    public OrderCreateRequest(
        Long orderTableId,
        List<OrderLineItemCreateRequest> orderLineItems
    ) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Order toEntity() {
        return new Order(null, orderTableId, OrderStatus.COOKING, LocalDateTime.now());
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemCreateRequest> getOrderLineItems() {
        return orderLineItems;
    }
}
