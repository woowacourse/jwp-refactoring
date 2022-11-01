package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "ORDERS")
public class Order {

    @Id
    private final Long id;
    private final Long orderTableId;
    private final OrderStatus orderStatus;
    private final LocalDateTime orderedTime;

    @MappedCollection(idColumn = "ORDER_ID")
    private final List<OrderLineItem> orderLineItems;

    public Order(final Long orderTableId, final String orderStatus, final LocalDateTime orderedTime,
                 final List<OrderLineItem> orderLineItems) {
        this(null, orderTableId, OrderStatus.from(orderStatus), orderedTime, orderLineItems);
    }

    @PersistenceCreator
    private Order(final Long id, final Long orderTableId, final OrderStatus orderStatus,
                  final LocalDateTime orderedTime, final List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static Order ofCooking(final OrderTable orderTable, final LocalDateTime orderedTime,
                                  final List<OrderLineItem> orderLineItems) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return new Order(orderTable.getId(), OrderStatus.COOKING.name(), orderedTime, orderLineItems);
    }

    public Order changeOrderStatus(final String orderStatus) {
        if (this.orderStatus.isCompleted()) {
            throw new IllegalArgumentException();
        }
        return new Order(id, orderTableId, OrderStatus.from(orderStatus), orderedTime, orderLineItems);
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus.name();
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
