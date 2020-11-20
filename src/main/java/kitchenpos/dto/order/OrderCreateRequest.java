package kitchenpos.dto.order;

import kitchenpos.domain.Order;

import java.time.LocalDateTime;

public class OrderCreateRequest {
    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private OrderLineItemCreateRequests orderLineItemCreateRequests;

    protected OrderCreateRequest() {
    }

    public OrderCreateRequest(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime, OrderLineItemCreateRequests orderLineItemCreateRequests) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItemCreateRequests = orderLineItemCreateRequests;
    }

    public OrderCreateRequest(Order order) {
        this(order.getId(), order.getOrderTableId(), order.getOrderStatus(), order.getOrderedTime(), OrderLineItemCreateRequests.from(order.getOrderLineItems()));
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public OrderLineItemCreateRequests getOrderLineItemRequests() {
        return orderLineItemCreateRequests;
    }

    public Order toEntity() {
        return new Order(this.id, this.orderTableId, this.orderStatus, this.orderedTime, this.orderLineItemCreateRequests.toOrderLineItems());
    }
}
