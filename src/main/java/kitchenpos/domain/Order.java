package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private Long id;
    private Long orderTableId;
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItem> orderLineItems;

    public Order(final Long id, final Long orderTableId, final String orderStatus, final LocalDateTime orderedTime,
                 final List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = OrderStatus.valueOf(orderStatus);
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static Order from(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("주문 테이블이 비어있습니다.");
        }
        return new Order(null, orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), null);
    }

    public static Order of(final Long id, final Long orderTableId, final String orderStatus, final LocalDateTime orderedTime) {
        return new Order(id, orderTableId, orderStatus, orderedTime, new ArrayList<>());
    }

    public static Order of(final Long orderTableId, final String orderStatus, final LocalDateTime orderedTime,
                 final List<OrderLineItem> orderLineItems) {
        return new Order(null, orderTableId, orderStatus, orderedTime, orderLineItems);
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus.name();
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public void changeStatus(final String status) {
        if (orderStatus.isCompletion()) {
            throw new IllegalArgumentException("주문이 이미 완료되었습니다.");
        }
        this.orderStatus = OrderStatus.valueOf(status);
    }
}
