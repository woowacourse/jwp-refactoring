package domain;

import exception.OrderException;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import domain.order_lineitem.OrderLineItems;
import support.AggregateReference;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;
    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "order_table_id"))
    private final AggregateReference<OrderTable> orderTable;
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
            final AggregateReference<OrderTable> orderTable,
            final OrderLineItems orderLineItems,
            final OrderValidator orderValidator,
            final LocalDateTime orderedTime
    ) {
        validateOrderLineItems(orderLineItems);
        this.id = null;
        this.orderTable = orderTable;
        this.orderStatus = OrderStatus.COOKING;
        this.orderLineItems = orderLineItems;
        this.orderedTime = orderedTime;
        orderValidator.validate(this);
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

    public AggregateReference<OrderTable> getOrderTable() {
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
