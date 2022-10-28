package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.springframework.util.CollectionUtils;

public class Order {

    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItem> orderLineItems;

    public Order() {
    }

    public Order(final Long orderTableId, final List<OrderLineItem> orderLineItems) {
        validateOrderLineItemIsNotEmpty(orderLineItems);
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Order(final Long id, final Long orderTableId, final String orderStatus, final LocalDateTime orderedTime, final List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public void changeStatus(final String orderStatus) {
        if (Objects.equals(OrderStatus.COMPLETION.name(), this.orderStatus)) {
            throw new IllegalArgumentException("주문이 완료된 상태입니다.");
        }
        this.orderStatus = orderStatus;
    }

    private void validateOrderLineItemIsNotEmpty(final List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException("주문 항목이 존재하지 않습니다.");
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

    public void setOrderStatus(final String orderStatus) {
        this.orderStatus = orderStatus;
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
