package kitchenpos.order.domain;

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

@Entity
@Table(name = "ORDERS")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.COOKING;

    private LocalDateTime orderedTime = LocalDateTime.now();

    @Embedded
    private final OrderLineItems orderLineItems = new OrderLineItems();

    public Order(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
        this.orderedTime = LocalDateTime.now();
    }

    public Order() {
    }

    public boolean isUngroupable() {
        return this.orderStatus.canUngroup();
    }

    public void changeStatus(String orderStatus) {
        if (this.orderStatus.isCompletion()) {
            throw new IllegalArgumentException("주문 상태가 계산 완료인 경우 변경할 수 없습니다.");
        }
        this.orderStatus = OrderStatus.valueOf(orderStatus);
    }

    public boolean isCookingOrMeal() {
        return this.orderStatus.isCookingOrMeal();
    }

    public void addOrderLineItem(OrderLineItem orderLineItem) {
        this.orderLineItems.add(orderLineItem);
    }

    public Long getId() {
        return id;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> showOrderLineItems() {
        return orderLineItems.showAll();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(getId(), order.getId()) &&
                getOrderStatus() == order.getOrderStatus() &&
                Objects.equals(getOrderedTime(), order.getOrderedTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getOrderStatus(), getOrderedTime());
    }
}
