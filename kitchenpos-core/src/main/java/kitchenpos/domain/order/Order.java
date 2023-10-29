package kitchenpos.domain.order;

import kitchenpos.domain.table.OrderTable;
import kitchenpos.exception.ExceptionInformation;
import kitchenpos.exception.KitchenposException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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
    private OrderLineItems orderLineItems;

    protected Order() {
    }

    public Order(final Long orderTableId, final OrderStatus orderStatus, final LocalDateTime orderedTime, final OrderLineItems orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static Order create(final OrderTable orderTable) {
        validateOrderTableIsNotEmpty(orderTable);

        return new Order(
                orderTable.getId(),
                OrderStatus.COOKING,
                LocalDateTime.now(),
                OrderLineItems.create()
        );
    }

    private static void validateOrderTableIsNotEmpty(final OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new KitchenposException(ExceptionInformation.ORDER_IN_EMPTY_TABLE);
        }
    }

    public void updateOrderLineItems(final List<OrderLineItem> orderLineItems) {
        this.orderLineItems = new OrderLineItems(orderLineItems);
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
        return orderLineItems.getOrderLineItems();
    }

    public void updateOrderStatus(final OrderStatus updateOrderStatus) {
        if (Objects.equals(OrderStatus.COMPLETION, orderStatus)) {
            throw new KitchenposException(ExceptionInformation.UPDATE_COMPLETED_ORDER);
        }
        this.orderStatus = updateOrderStatus;
    }
}
