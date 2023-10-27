package kitchenpos.domain.order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import kitchenpos.domain.order.exception.OrderException;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_table_id")
    private Long orderTableId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;

    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id", nullable = false)
    private List<OrderLineItem> orderLineItems;

    protected Order() {
    }

    private Order(final Long orderTableId, final OrderStatus orderStatus,
                  final LocalDateTime orderedTime, final List<OrderLineItem> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }


    public static Order from(final Long orderTableId, final int orderLineItemSize,
                             final int foundMenuSize, final List<OrderLineItem> orderLineItems) {
        validateOrderLineItemSize(orderLineItemSize, foundMenuSize);
        return new Order(orderTableId, OrderStatus.COOKING, LocalDateTime.now(), orderLineItems);
    }

    private static void validateOrderLineItemSize(final int OrderLineItemSize, final int foundMenuSize) {
        if (OrderLineItemSize != foundMenuSize) {
            throw new OrderException.NotFoundOrderLineItemMenuExistException();
        }
    }

    public void changeStatus(final OrderStatus orderStatus) {
        validateAvailableChangeStatus();
        this.orderStatus = orderStatus;
    }

    private void validateAvailableChangeStatus() {
        if (Objects.equals(OrderStatus.COMPLETION, this.orderStatus)) {
            throw new OrderException.CannotChangeOrderStatusByCurrentOrderStatusException();
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
}
