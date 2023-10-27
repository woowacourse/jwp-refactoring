package kitchenpos.order;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
import kitchenpos.exception.OrderException.EmptyOrderLineItemsException;
import kitchenpos.exception.OrderException.EmptyOrderTableException;

@Entity(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderTableId;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime orderedTime;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "order_id", updatable = false, nullable = false)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected Order() {
    }

    private Order(final Long orderTableId, final OrderStatus orderStatus,
        final LocalDateTime orderedTime, final List<OrderLineItem> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static Order of(final Long orderTableId,
        final OrderTableChangeService orderTableChangeService,
        final List<OrderLineItem> orderLineItems) {
        if (orderTableChangeService.isNotEmpty(orderTableId)) {
            throw new EmptyOrderTableException();
        }

        if (orderLineItems.isEmpty()) {
            throw new EmptyOrderLineItemsException();
        }

        return new Order(orderTableId, OrderStatus.COOKING, LocalDateTime.now(), orderLineItems);
    }

    public void changeOrderStatus(final OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public boolean isSameStatus(final OrderStatus orderStatus) {
        return this.orderStatus == orderStatus;
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
