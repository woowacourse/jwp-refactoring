package kitchenpos.builder;

import java.time.LocalDateTime;
import java.util.List;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

public class OrderBuilder {
    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItem> orderLineItems;

    public OrderBuilder() {
    }

    public OrderBuilder(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime,
        List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public OrderBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public OrderBuilder orderTableId(Long orderTableId) {
        this.orderTableId = orderTableId;
        return this;
    }

    public OrderBuilder orderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
        return this;
    }

    public OrderBuilder orderedTime(LocalDateTime orderedTime) {
        this.orderedTime = orderedTime;
        return this;
    }

    public OrderBuilder orderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
        return this;
    }

    public Order build() {
        return new Order(id, orderTableId, orderStatus, orderedTime, orderLineItems);
    }
}
