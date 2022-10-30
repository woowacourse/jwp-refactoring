package kitchenpos.domain.order;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Order {

    private final Long id;
    private final Long orderTableId;
    private OrderStatus orderStatus;
    private final LocalDateTime orderedTime;
    private final OrderLineItems orderLineItems;

    /**
     * DB 에 저장되지 않은 객체
     */
    public Order(final Long orderTableId, final OrderStatus orderStatus, final LocalDateTime orderedTime, final List<OrderLineItem> orderLineItems) {
        this(null, orderTableId, orderStatus, orderedTime, orderLineItems);
    }

    /**
     * DB 에 저장된 객체
     */
    public Order(final Long id, final Long orderTableId, final OrderStatus orderStatus, final LocalDateTime orderedTime) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = null;
    }

    public Order(final Long id, final Long orderTableId, final OrderStatus orderStatus, final LocalDateTime orderedTime, final List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = new OrderLineItems(orderLineItems);
    }

    public void changeStatus(final OrderStatus orderStatus) {
        if (Objects.equals(OrderStatus.COMPLETION, this.orderStatus)) {
            throw new IllegalArgumentException("주문이 완료된 상태입니다.");
        }
        this.orderStatus = orderStatus;
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
        if (orderLineItems == null) {
            return Collections.emptyList();
        }
        return orderLineItems.getValue();
    }
}
