package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderTableId;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItemGroup orderLineItems;

    protected Order() {
    }

    public static Order create(long id, long orderTableId, String orderStatus,
                               LocalDateTime orderedTime) {
        final Order order = new Order();
        order.id = id;
        order.orderTableId = orderTableId;
        order.orderStatus = OrderStatus.valueOf(orderStatus);
        order.orderedTime = orderedTime;
        return order;
    }

    public static Order createSingleId(long orderId) {
        final Order order = new Order();
        order.id = orderId;
        return order;
    }

    public static Order create(Long orderTableId, List<OrderLineItem> orderLineItems) {
        final Order order = new Order();
        order.orderTableId = orderTableId;
        order.orderLineItems = OrderLineItemGroup.from(orderLineItems, order);
        return order;
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
        return orderLineItems.value();
    }

    public void startOrder() {
        this.orderStatus = OrderStatus.COOKING;
        this.orderedTime = LocalDateTime.now();
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        if (Objects.equals(OrderStatus.COMPLETION, this.orderStatus)) {
            throw new IllegalArgumentException();
        }
        this.orderStatus = orderStatus;
    }

    public boolean unableUngroup() {
        return orderStatus.equals(OrderStatus.COOKING) || orderStatus.equals(OrderStatus.MEAL);
    }
}
