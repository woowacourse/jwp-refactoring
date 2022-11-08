package kitchenpos.order.domain;

import static kitchenpos.order.domain.OrderStatus.COOKING;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "orderLineItem",
            joinColumns = @JoinColumn(name = "orderId")
    )
    private List<OrderLineItem> orderLineItems;

    protected Order() {
    }

    public Order(final Long orderTableId, final LocalDateTime orderedTime, final List<OrderLineItem> items) {
        this(null, orderTableId, COOKING, orderedTime, items);
    }

    private Order(final Long id, final Long orderTableId, final OrderStatus orderStatus, final LocalDateTime orderedTime,
                 final List<OrderLineItem> orderLineItems) {
        if (orderLineItems.isEmpty()) {
            throw new IllegalArgumentException();
        }

        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public Order(final String orderStatus) {
        this.orderStatus = OrderStatus.valueOf(orderStatus);
    }

    public void changeStatus(final OrderStatus nextStatus) {
        if (!orderStatus.isPossibleNextStatus(nextStatus)) {
            throw new IllegalArgumentException();
        }

        orderStatus = nextStatus;
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
