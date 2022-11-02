package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItem> orderLineItems;



    public Order(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
    }

    public Order(Long orderTableId, List<OrderLineItem> orderLineItems) {
        this.orderTableId = orderTableId;
        validateOrderLineItem(orderLineItems);
        this.orderLineItems = orderLineItems;
    }

    public Order(Long orderTableId, String orderStatus, LocalDateTime orderedTime,
                 List<OrderLineItem> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        validateOrderLineItem(orderLineItems);
        this.orderLineItems = orderLineItems;
    }

    public Order(Long orderTableId, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderStatus = OrderStatus.COOKING.name();
        this.orderedTime = orderedTime;
        validateOrderLineItem(orderLineItems);
        this.orderLineItems = orderLineItems;
    }

    private void validateOrderLineItem(List<OrderLineItem> orderLineItems) {
        if (orderLineItems.isEmpty()) {
            throw new IllegalArgumentException("주문 항목은 비어 있을 수 없습니다.");
        }
    }

    public void changeStatus(String beChangeOrderStatus) {
        if (orderStatus.equals(OrderStatus.COMPLETION.name())) {
            throw new IllegalArgumentException("주문이 이미 완료됐습니다.");
        }
        this.orderStatus = OrderStatus.valueOf(beChangeOrderStatus).name();
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
