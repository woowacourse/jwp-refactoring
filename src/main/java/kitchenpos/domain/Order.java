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

    public Order(Long orderTableId) {
        validateEmptyOrderTableId(orderTableId);
        this.orderTableId = orderTableId;
        this.orderStatus = OrderStatus.COOKING.name();
        this.orderedTime = LocalDateTime.now();
    }

    public Order(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
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
        validateOrderLineItems(orderLineItems);
        this.orderLineItems = orderLineItems;
    }

    public void changeStatus(String status) {
        if (Objects.equals(OrderStatus.COMPLETION.name(), orderStatus)) {
            throw new IllegalArgumentException("이미 완료된 주문 상태를 변경할 수 없습니다.");
        }
        orderStatus = status;
    }

    private void validateEmptyOrderTableId(Long orderTableId) {
        if (orderTableId == null) {
            throw new IllegalArgumentException("주문 테이블은 비어있을 수 없습니다.");
        }
    }

    private void validateOrderLineItems(List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException("주문 항목은 비어있을 수 없습니다.");
        }
    }
}
