package kitchenpos.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

public class OrderCreateRequest {

    private Long orderTableId;
    private List<OrderLineItemCreateRequest> orderLineItems;

    private OrderCreateRequest() {
    }

    public OrderCreateRequest(Long orderTableId, List<OrderLineItemCreateRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemCreateRequest> getOrderLineItems() {
        return orderLineItems;
    }

    public Order toEntity(String name, LocalDateTime now) {
        return new Order(orderTableId, name, now, toOrderLineItemEntity());
    }

    private List<OrderLineItem> toOrderLineItemEntity() {
        return orderLineItems.stream()
                .map(request -> new OrderLineItem(null, request.getMenuId(), request.getQuantity()))
                .collect(Collectors.toList());
    }
}
