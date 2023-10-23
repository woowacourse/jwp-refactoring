package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.CascadeType;
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

@Table(name = "orders")
@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long orderTableId;
    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id", nullable = false)
    private List<OrderLineItem> orderLineItems;

    protected Order() {
    }

    public Order(
            final Long id,
            final Long orderTableId,
            final OrderStatus orderStatus,
            final LocalDateTime orderedTime,
            final List<OrderLineItem> orderLineItems,
            final OrderValidator orderValidator
    ) {
        orderValidator.validateExistTable(orderTableId);
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public Order(
            final Long orderTableId,
            final List<OrderLineItem> orderLineItems,
            final OrderValidator orderValidator
    ) {
        this(null, orderTableId, OrderStatus.COOKING, LocalDateTime.now(), orderLineItems, orderValidator);
    }

    public void changeOrderStatus(final OrderStatus orderStatus) {
        if (this.orderStatus == OrderStatus.COMPLETION) {
            throw new IllegalArgumentException("Order already completed cannot be changed");
        }
        this.orderStatus = orderStatus;
    }

    public boolean isCompleted() {
        return this.orderStatus == OrderStatus.COMPLETION;
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
