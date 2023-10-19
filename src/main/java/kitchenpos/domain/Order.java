package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;

import static kitchenpos.domain.OrderStatus.*;

public class Order {
    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItem> orderLineItems;

    public Order() {
    }

    public Order(final Long id, final Long orderTableId, final String orderStatus, final LocalDateTime orderedTime, final List<OrderLineItem> orderLineItems) {
        // TODO: orderLineItems의 orderId 변경하기
        // TODO: orderLineItems의 menuId 변경하기
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public Order(final Long orderTableId, final List<OrderLineItem> orderLineItems) {
        this(null, orderTableId, COOKING.name(), LocalDateTime.now(), orderLineItems);
    }

    private void validateOrderLineItemsSize(final int orderLineItemsSize) {
        if (orderLineItemsSize < 0) {
            throw new IllegalArgumentException("주문 항목은 0개 이상이어야 합니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public void setOrderTableId(final Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void changeOrderStatus(final String orderStatus) {
        // TODO: meal -> cooking의 상태를 가능하게 할 것인가?
        if (this.orderStatus.equals(COMPLETION.name())) {
            throw new IllegalArgumentException("이미 완료된 주문은 변경할 수 없습니다.");
        }
        this.orderStatus = orderStatus;
        this.orderedTime = LocalDateTime.now();
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public void setOrderedTime(final LocalDateTime orderedTime) {
        this.orderedTime = orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public void setOrderLineItems(final List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }
}
