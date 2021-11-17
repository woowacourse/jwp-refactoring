package kitchenpos.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;

public class OrderRequest {

    private Long id;
    private Long orderTableId;
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemRequest> orderLineItemRequests;

    public OrderRequest(Long id,
                        Long orderTableId,
                        OrderStatus orderStatus,
                        LocalDateTime orderedTime,
                        List<OrderLineItemRequest> orderLineItemRequests
    ) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItemRequests = orderLineItemRequests;
    }

    public Order toOrder() {
        List<OrderLineItem> orderLineItems = orderLineItemRequests.stream()
            .map(OrderLineItemRequest::toOrderLineItem)
            .collect(Collectors.toList());
        return new Order(orderTableId, orderStatus, orderedTime, orderLineItems);
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItemRequest> getOrderLineItemRequests() {
        return orderLineItemRequests;
    }
}
