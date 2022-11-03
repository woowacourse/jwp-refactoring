package kitchenpos.order.domain;

import static kitchenpos.order.domain.OrderStatus.COOKING;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import kitchenpos.core.event.Events;
import kitchenpos.table.domain.OrderTable;
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

    public Order(final Long orderTableId, final OrderStatus orderStatus, final LocalDateTime orderedTime,
                 final List<OrderLineItem> orderLineItems) {
        this(null, orderTableId, orderStatus, orderedTime, orderLineItems);
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
        Events.raise(new OrderStatusChangedEvent(orderTable.getId(), COOKING));
        return new Order(orderTable.getId(), COOKING, orderedTime, orderLineItems);
    }

    public boolean isLegacy() {
        return orderLineItems.stream()
                .map(OrderLineItem::getOrderMenu)
                .map(OrderMenu::getMenuName)
                .anyMatch(Objects::isNull);
    }

    public Order changeOrderStatus(final OrderStatus orderStatus) {
        if (this.orderStatus.isCompleted()) {
            throw new IllegalArgumentException();
        }
        Events.raise(new OrderStatusChangedEvent(orderTableId, orderStatus));
        return new Order(id, orderTableId, orderStatus, orderedTime, orderLineItems);
    }

    public Order replaceOrderLineItems(final List<OrderLineItem> orderLineItems) {
        return new Order(id, orderTableId, orderStatus, orderedTime, orderLineItems);
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
