package kitchenpos.order.domain;

import java.time.LocalDateTime;
import kitchenpos.ordertable.domain.OrderTable;
import org.springframework.data.annotation.Id;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.data.relational.core.mapping.Embedded;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "ORDERS")
public class Order {
    @Id
    private Long id;
    private AggregateReference<OrderTable, Long> orderTableId;
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    @Embedded.Empty
    private OrderLineItems orderLineItems;

    private Order() {
    }

    public Order(Long orderTableId, OrderLineItems orderLineItems) {
        this(null, orderTableId, OrderStatus.COOKING, LocalDateTime.now(), orderLineItems);
    }

    private Order(
            Long id,
            Long orderTableId,
            OrderStatus orderStatus,
            LocalDateTime orderedTime,
            OrderLineItems orderLineItems
    ) {
        this.id = id;
        this.orderTableId = AggregateReference.to(orderTableId);
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public boolean isComplete() {
        return orderStatus.isComplete();
    }

    public void updateOrderStatus(OrderStatus desiredStatus) {
        if (desiredStatus != this.orderStatus.next()) {
            throw new IllegalArgumentException("주문 상태를 변경할 수 없습니다.");
        }
        this.orderStatus = desiredStatus;
    }

    public Long getId() {
        return id;
    }

    public AggregateReference<OrderTable, Long> getOrderTableId() {
        return orderTableId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public OrderLineItems getOrderLineItems() {
        return orderLineItems;
    }
}
