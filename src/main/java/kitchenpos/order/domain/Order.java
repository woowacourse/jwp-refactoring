package kitchenpos.order.domain;

import kitchenpos.order.exception.OrderException;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Table(name = "orders")
@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_table_id")
    private Long orderTableId;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.COOKING;

    @Embedded
    private OrderLineItems orderLineItems;

    private LocalDateTime orderedTime;

    protected Order() {
    }

    public Order(Long orderTableId, List<OrderLineItem> orderLineItems, LocalDateTime orderedTime) {
        this.orderTableId = orderTableId;
        this.orderLineItems = new OrderLineItems(orderLineItems);
        this.orderedTime = orderedTime;
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        if (isCompleted()) {
            throw new OrderException("이미 완료된 주문이라 주문 상태를 변경할 수 없습니다.");
        }
        this.orderStatus = orderStatus;
    }

    public void completeOrder() {
        this.orderStatus = OrderStatus.COMPLETION;
    }

    public boolean isCompleted() {
        return orderStatus == OrderStatus.COMPLETION;
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.getOrderLineItems();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(getId(), order.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
