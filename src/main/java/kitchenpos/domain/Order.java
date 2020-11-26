package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

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

    public Order() {
    }

    public Order(Long id, OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime,
        List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static Order of(OrderTable orderTable, OrderStatus orderStatus,
        LocalDateTime orderedTime,
        List<OrderLineItem> orderLineItems) {
        return new Order(null, orderTable, orderStatus, orderedTime, orderLineItems);
    }

    public static Order of(OrderTable orderTable, OrderStatus orderStatus,
        LocalDateTime orderedTime) {
        validateOrderTable(orderTable);
        return new Order(null, orderTable, orderStatus, orderedTime, null);
    }

    private static void validateOrderTable(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("OrderTable이 비어있습니다.");
        }
    }

    public void updateOrder(Order savedOrder, OrderStatus orderStatus,
        List<OrderLineItem> orderLineItems) {
        validateStatus(savedOrder.orderStatus);

        this.id = savedOrder.getId();
        this.orderTable = savedOrder.getOrderTable();
        this.orderStatus = orderStatus;
        this.orderedTime = savedOrder.getOrderedTime();
        this.orderLineItems = orderLineItems;
    }

    private void validateStatus(OrderStatus orderStatus) {
        if (Objects.equals(OrderStatus.COMPLETION, orderStatus)) {
            throw new IllegalArgumentException("완료된 주문입니다.");
        }
    }

    public void updateOrder(List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
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
}
