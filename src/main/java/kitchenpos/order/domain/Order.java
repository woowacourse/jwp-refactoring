package kitchenpos.order.domain;

import kitchenpos.ordertable.domain.OrderTable;

import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItem> orderLineItems;

    public Order(
            Long id,
            Long orderTableId,
            String orderStatus,
            LocalDateTime orderedTime,
            List<OrderLineItem> orderLineItems
    ) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static Order create(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        if (orderLineItems.isEmpty()) {
            throw new IllegalArgumentException("주문 항목이 포함되어 있지 않습니다.");
        }
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("비어 있는 테이블은 주문을 생성할 수 없습니다.");
        }
        return new Order(null, orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), orderLineItems);
    }

    public void assignOrderToTable() {
        for (OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.assignMenuId(this.id);
        }
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        if (this.orderStatus.equals(OrderStatus.COMPLETION.name())) {
            throw new IllegalArgumentException("계산 완료된 주문은 주문 상태를 변경할 수 없습니다.");
        }
        this.orderStatus = orderStatus.name();
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
