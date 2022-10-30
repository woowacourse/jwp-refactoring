package kitchenpos.domain.order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.springframework.util.CollectionUtils;

public class Order {

    private final Long id;
    private final Long orderTableId;
    private String orderStatus;
    private final LocalDateTime orderedTime;
    private List<OrderLineItem> orderLineItems;

    /**
     * DB 에 저장되지 않은 객체
     */
    public Order(final Long orderTableId, final String orderStatus, final LocalDateTime orderedTime, final List<OrderLineItem> orderLineItems) {
        this(null, orderTableId, orderStatus, orderedTime, orderLineItems);
    }

    /**
     * DB 에 저장된 객체
     */
    public Order(final Long id, final Long orderTableId, final String orderStatus, final LocalDateTime orderedTime) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
    }

    public Order(final Long id, final Long orderTableId, final String orderStatus, final LocalDateTime orderedTime, final List<OrderLineItem> orderLineItems) {
        validateOrderLineItemIsNotEmpty(orderLineItems);
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

    public void setOrderLineItems(final List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }
}
