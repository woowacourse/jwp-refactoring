package kitchenpos.domain.order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import kitchenpos.domain.common.OrderStatus;
import kitchenpos.exception.badrequest.CompletedOrderCannotChangeException;
import kitchenpos.exception.badrequest.OrderLineItemNotExistsException;
import kitchenpos.exception.badrequest.OrderTableEmptyException;
import org.hibernate.annotations.BatchSize;
import org.springframework.util.CollectionUtils;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "order_table_id", nullable = false)
    private Long orderTableId;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus;
    @Column(name = "ordered_time", nullable = false)
    private LocalDateTime orderedTime;
    @BatchSize(size = 100)
    @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected Order() {
    }

    public Order(final Long orderTableId, final List<OrderLineItem> orderLineItems, final boolean orderTableEmpty) {
        this(null, orderTableId, OrderStatus.COOKING, LocalDateTime.now(), orderLineItems, orderTableEmpty);
    }

    public Order(final Long id, final Long orderTableId, final OrderStatus orderStatus, final LocalDateTime orderedTime,
                 final List<OrderLineItem> orderLineItems, final boolean orderTableEmpty) {
        validateTableNotEmpty(orderTableEmpty);
        mapOrderLineItems(orderLineItems);
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
    }

    private void validateTableNotEmpty(final boolean orderTableEmpty) {
        if (orderTableEmpty) {
            throw new OrderTableEmptyException();
        }
    }

    private void mapOrderLineItems(final List<OrderLineItem> orderLineItems) {
        validateItemExists(orderLineItems);
        this.orderLineItems = orderLineItems;
        for (OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.mapOrder(this);
        }
    }

    private void validateItemExists(final List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new OrderLineItemNotExistsException();
        }
    }

    public void changeOrderStatus(final String orderStatusName) {
        validateNotCompleted();
        this.orderStatus = OrderStatus.valueOf(orderStatusName);
    }

    private void validateNotCompleted() {
        if (orderStatus == OrderStatus.COMPLETION) {
            throw new CompletedOrderCannotChangeException();
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
        if (this == o) {
            return true;
        }
        if (!(o instanceof Order)) {
            return false;
        }
        Order order = (Order) o;
        return Objects.equals(id, order.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
