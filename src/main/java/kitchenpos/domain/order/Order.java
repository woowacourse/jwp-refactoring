package kitchenpos.domain.order;

import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.exception.OrderException;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;
    @ManyToOne
    private final OrderTable orderTable;
    @Embedded
    private final OrderLineItems orderLineItems;
    private final LocalDateTime orderedTime;
    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    protected Order() {
        id = null;
        orderTable = null;
        orderLineItems = null;
        orderedTime = null;
    }

    public Order(
            final OrderTable orderTable,
            final OrderLineItems orderLineItems
    ) {
        validateOrderTable(orderTable);
        validateOrderLineItems(orderLineItems);
        this.id = null;
        this.orderTable = orderTable;
        this.orderStatus = OrderStatus.COOKING;
        this.orderLineItems = orderLineItems;
        this.orderedTime = LocalDateTime.now();
    }

    private void validateOrderTable(final OrderTable orderTable) {
        if (Objects.isNull(orderTable)) {
            throw new OrderException.NoTableException();
        }

        if (orderTable.isEmpty()) {
            throw new OrderException.EmptyTableException();
        }
    }

    private void validateOrderLineItems(final OrderLineItems orderLineItems) {
        if (Objects.isNull(orderLineItems)) {
            throw new OrderException.NoOrderLineItemsException();
        }
    }

    public void changeOrderStatus(final OrderStatus orderStatus) {
        this.orderStatus = this.orderStatus.changeOrderStatus(orderStatus);
    }

    public boolean isStatusOf(final OrderStatus status) {
        return this.orderStatus.equals(status);
    }

    public Long getId() {
        return id;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }

    public OrderLineItems getOrderLineItems() {
        return orderLineItems;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
