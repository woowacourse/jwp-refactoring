package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class Order {
    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItem> orderLineItems;

    private Order(final Long id,
                 final Long orderTableId,
                 final String orderStatus,
                 final LocalDateTime orderedTime,
                 final List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static Order of(final Long id,
                           final Long orderTableId,
                           final String orderStatus,
                           final LocalDateTime orderedTime,
                           final List<OrderLineItem> orderLineItems) {
        if (orderLineItems == null) {
            throw new IllegalArgumentException("주문 상품 목록이 없으면 주문을 생성할 수 없습니다.");
        }
        if (orderLineItems.isEmpty()) {
            throw new IllegalArgumentException("주문 상품 목록이 없으면 주문을 생성할 수 없습니다.");
        }
        return new Order(id, orderTableId, orderStatus, orderedTime, orderLineItems);
    }

    public static Order of(final Long orderTableId, final List<OrderLineItem> orderLineItems) {
        return of(null, orderTableId, OrderStatus.COOKING.name(), LocalDateTime.now(), orderLineItems);
    }

    public static Order createForEntity(final Long id,
                                        final Long orderTableId,
                                        final String orderStatus,
                                        final LocalDateTime orderedTime) {
        return new Order(id, orderTableId, orderStatus, orderedTime, new LinkedList<>());
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

    public void changeOrderStatus(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public void setOrderLineItems(final List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }
}
