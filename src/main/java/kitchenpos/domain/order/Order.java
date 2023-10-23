package kitchenpos.domain.order;

import kitchenpos.exception.OrderException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Table(name = "orders")
@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private OrderTable orderTable;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.COOKING;

    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems;

    protected Order() {
    }

    public Order(LocalDateTime orderedTime) {
        this.orderedTime = orderedTime;
        this.orderLineItems = new OrderLineItems();
    }

    public void changeOrderTable(OrderTable orderTable) {
        this.orderTable = orderTable;
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

    public void addOrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems.add(orderLineItems);
        for (OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.changeOrder(this);
        }
    }

    public Long getId() {
        return id;
    }

    public OrderTable getOrderTable() {
        return orderTable;
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
