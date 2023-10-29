package kitchenpos.order.domain;

import kitchenpos.order.domain.validator.OrderValidator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Table(name = "orders")
@Entity
public class Order {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private Long orderTableId;
    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected Order() {
    }

    private Order(final Long orderTableId,
                  final OrderStatus orderStatus,
                  final LocalDateTime orderedTime) {
        this(null, orderTableId, orderStatus, orderedTime);
    }

    private Order(final Long id,
                  final Long orderTableId,
                  final OrderStatus orderStatus,
                  final LocalDateTime orderedTime) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
    }

    public static Order create(final Long orderTableId,
                               final List<OrderLineItem> orderLineItems,
                               final OrderValidator orderValidator) {
        orderValidator.validateNotEmptyOrderTable(orderTableId);
        final Order order = new Order(
                orderTableId,
                OrderStatus.COOKING,
                LocalDateTime.now());
        order.addOrderLineItems(orderLineItems);
        return order;
    }

    private void addOrderLineItems(final List<OrderLineItem> orderLineItems) {
        for (OrderLineItem orderLineItem : orderLineItems) {
            addOrderLineItem(orderLineItem);
        }
    }

    private void addOrderLineItem(final OrderLineItem orderLineItem) {
        orderLineItem.setOrder(this);
        this.orderLineItems.add(orderLineItem);
    }

    public void changeOrderStatus(final OrderStatus orderStatus) {
        validateInProgressing();
        this.orderStatus = orderStatus;
    }

    private void validateInProgressing() {
        if (OrderStatus.COMPLETION == orderStatus) {
            throw new IllegalArgumentException();
        }
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
        return orderLineItems;
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
                ", orderTableId=" + orderTableId +
                ", orderStatus='" + orderStatus + '\'' +
                ", orderedTime=" + orderedTime +
                ", orderLineItems.size=" + orderLineItems.size() +
                '}';
    }
}
