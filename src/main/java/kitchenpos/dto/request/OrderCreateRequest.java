package kitchenpos.dto.request;

import java.util.List;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;

public class OrderCreateRequest {

    private Long orderTableId;

    private List<OrderLineItemCreateRequest> orderLineItems;

    public OrderCreateRequest(Long orderTableId,
        List<OrderLineItemCreateRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Order toEntity() {
        return new Order(null, orderTableId, OrderStatus.COOKING.name());
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemCreateRequest> getOrderLineItems() {
        return orderLineItems;
    }
}
