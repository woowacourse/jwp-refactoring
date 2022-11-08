package kitchenpos.domain.order;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import kitchenpos.exception.CustomError;
import kitchenpos.exception.DomainLogicException;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_table_id")
    private Long orderTableId;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime orderedTime;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id", nullable = false, updatable = false)
    private List<OrderLineItem> orderLineItems;

    protected Order() {
    }

    public Order(final Long orderTableId, final OrderStatus orderStatus, final LocalDateTime orderedTime,
                 final List<OrderLineItem> orderLineItems) {
        this(null, orderTableId, orderStatus, orderedTime, orderLineItems);
    }

    public Order(final Long id, final Long orderTableId, final OrderStatus orderStatus,
                 final LocalDateTime orderedTime, final List<OrderLineItem> orderLineItems) {
        validateOrderLineItemsNotEmpty(orderLineItems);
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    private void validateOrderLineItemsNotEmpty(final List<OrderLineItem> items) {
        if (items == null || items.isEmpty()) {
            throw new DomainLogicException(CustomError.ORDER_ITEM_EMPTY_ERROR);
        }
    }

    public void changeStatus(final OrderStatus status) {
        this.orderStatus = this.orderStatus.change(status);
    }

    public boolean isCompleted() {
        return this.orderStatus.isSame(OrderStatus.COMPLETION);
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return this.orderTableId;
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
