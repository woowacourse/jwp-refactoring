package kitchenpos.application.dto.request;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

public class OrderCreateRequest {

    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItem> orderLineItems;

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

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public OrderCreateRequest() {
    }

    public OrderCreateRequest(final Long id, final Long orderTableId, final String orderStatus,
                              final LocalDateTime orderedTime,
                              final List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public Order toOrder(final Long orderTableId, final String orderStatus, final LocalDateTime orderedTime) {
        return new Order(
                id,
                orderTableId,
                orderStatus,
                orderedTime,
                orderLineItems
        );
    }
}
