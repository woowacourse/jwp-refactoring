package kitchenpos.domain.order;

import kitchenpos.domain.table.OrderTable;
import kitchenpos.exception.EmptyOrderTableException;
import kitchenpos.exception.OrderStatusNotChangeableException;
import kitchenpos.util.ValidateUtil;

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

    @OneToOne
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;

    @OneToMany(mappedBy = "order")
    private List<OrderLineItem> orderLineItems;

    protected Order() {
    }

    public Order(OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime) {
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
    }

    public static Order from(OrderTable orderTable) {
        ValidateUtil.validateNonNull(orderTable);
        validateOrderTable(orderTable);
        OrderStatus orderStatus = OrderStatus.COOKING;
        LocalDateTime orderedTime = LocalDateTime.now();

        return new Order(orderTable, orderStatus, orderedTime);
    }

    private static void validateOrderTable(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new EmptyOrderTableException();
        }
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        ValidateUtil.validateNonNull(orderStatus);
        if (Objects.equals(OrderStatus.COMPLETION, this.orderStatus)) {
            throw new OrderStatusNotChangeableException();
        }

        this.orderStatus = orderStatus;
    }

    public Long getId() {
        return id;
    }

    public OrderTable getOrderTable() {
        return orderTable;
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

    public void setOrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }
}
