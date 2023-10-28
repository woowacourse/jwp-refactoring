package kitchenpos.order.domain;

import kitchenpos.common.vo.OrderStatus;
import kitchenpos.exception.InvalidOrderStatusToChangeException;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long orderTableId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;

    @Column(nullable = false)
    private LocalDateTime orderedTime;

    @Embedded
    @AttributeOverride(name = "values", column = @Column(name = "orderLineItems"))
    private OrderLineItems orderLineItems;

    protected Order() {}

    private Order(
            final Long orderTableId,
            final OrderStatus orderStatus,
            final LocalDateTime now,
            final OrderLineItems orderLineItems
    ) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = now;
        this.orderLineItems = orderLineItems;
    }

    public static Order of(
            final Long orderTableId,
            final OrderStatus orderStatus,
            final LocalDateTime now,
            final OrderLineItems orderLineItems
    ) {
        orderLineItems.validateOrderLineItems();

        return new Order(orderTableId, orderStatus, now, orderLineItems);
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

    public void updateOrderStatus(final OrderStatus orderStatus) {
        validateOrderStatus();
        this.orderStatus = orderStatus;
    }

    private void validateOrderStatus() {
        if (Objects.equals(OrderStatus.COMPLETION, orderStatus)) {
            throw new InvalidOrderStatusToChangeException("주문이 상태가 계산 완료라면 상태를 변경할 수 없다.");
        }
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.getValues();
    }

    public void updateOrderLineItems(final OrderLineItems orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Order{" +
               "id=" + id +
               ", orderStatus='" + orderStatus + '\'' +
               ", orderedTime=" + orderedTime +
               '}';
    }
}
