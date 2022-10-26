package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order {

    private Long id;
    private final Long orderTableId;
    private String orderStatus;
    private final LocalDateTime orderedTime;
    private final List<OrderLineItem> orderLineItems;

    public Order(Long orderTableId, String orderStatus, LocalDateTime orderedTime) {
        this(orderTableId, orderStatus, orderedTime, new ArrayList<>());
    }

    public Order(Long orderTableId, String orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        this(null, orderTableId, orderStatus, orderedTime, orderLineItems);
    }

    public Order(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime) {
        this(id, orderTableId, orderStatus, orderedTime, new ArrayList<>());
    }

    public Order(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public void addOrderLineItems(List<OrderLineItem> orderLineItems) {
        validateItemSize(orderLineItems);
        this.orderLineItems.addAll(orderLineItems);
    }

    private void validateItemSize(List<OrderLineItem> orderLineItems) {
        if (orderLineItems.isEmpty()) {
            throw new IllegalArgumentException("메뉴 상품이 1개 이상 있어야 합니다.");
        }
    }

    public void updateStatus(String orderStatus) {
        validateOrderStateComplete(this.orderStatus);
        this.orderStatus = orderStatus;
    }

    private void validateOrderStateComplete(String orderStatus) {
        if (OrderStatus.COMPLETION.name().equals(orderStatus)) {
            throw new IllegalArgumentException("완료 주문은 상태를 변경할 수 없습니다.");
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
}
