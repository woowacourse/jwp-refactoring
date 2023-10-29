package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "orders")
@Entity
public class Order {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "order_table_id", nullable = false)
    private Long orderTableId;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.COOKING;

    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems;

    public Order() {
    }

    public Order(final Long orderTableId, final LocalDateTime orderedTime) {
        this.orderTableId = orderTableId;
        this.orderedTime = orderedTime;
    }

    public Long getId() {
        return id;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void updateOrderStatus(final OrderStatus orderStatus) {
        if (OrderStatus.COMPLETION.equals(this.orderStatus)) {
            throw new IllegalArgumentException("이미 계산 완료되었으면 주문 상태를 변경할 수 없습니다.");
        }
        this.orderStatus = orderStatus;
    }

    public void updateOrderLineItems(final List<OrderLineItem> orderLineItems) {
        this.orderLineItems = OrderLineItems.from(orderLineItems);
    }

    public boolean isCompleted() {
        return OrderStatus.COMPLETION.equals(orderStatus);
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItemsValue() {
        return orderLineItems.getItems();
    }
}
