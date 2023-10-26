package kitchenpos.domain.order;

import kitchenpos.application.order.request.OrderLineItemCreateRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static kitchenpos.domain.order.OrderStatus.*;

public class Order {
    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    public Order(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
    }

    public static Order of(Long orderTableId, OrderValidator validator, List<OrderLineItemCreateRequest> orderLineItemCreateRequests) {
        validator.validate(orderTableId, orderLineItemCreateRequests);
        return new Order(null, orderTableId, COOKING.name(), LocalDateTime.now());
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

    public void changeOrderStatus(String orderStatus) {
        validateOrderStatus();
        this.orderStatus = orderStatus;
    }

    private void validateOrderStatus() {
        if (isCompleted(orderStatus)) {
            throw new IllegalArgumentException();
        }
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public void setOrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }
}
