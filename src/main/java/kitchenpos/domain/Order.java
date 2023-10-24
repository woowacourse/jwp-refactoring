package kitchenpos.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Table(name = "orders")
@Entity
public class Order {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @JoinColumn(name = "order_table_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private OrderTable orderTable;
    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    public Order() {
    }

    private Order(final OrderTable orderTable,
                  final OrderStatus orderStatus,
                  final LocalDateTime orderedTime) {
        this(null, orderTable, orderStatus, orderedTime);
    }

    private Order(final Long id,
                  final OrderTable orderTable,
                  final OrderStatus orderStatus,
                  final LocalDateTime orderedTime) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
    }

    public static Order create(final OrderTable orderTable,
                               final List<OrderLineItem> orderLineItems) {
        validateNotEmptyOrderTable(orderTable);
        final Order order = new Order(
                orderTable,
                OrderStatus.COOKING,
                LocalDateTime.now());
        order.addOrderLineItems(orderLineItems);
        return order;
    }

    private static void validateNotEmptyOrderTable(final OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private void addOrderLineItems(final List<OrderLineItem> orderLineItems) {
        for (OrderLineItem orderLineItem : orderLineItems) {
            addOrderLineItem(orderLineItem);
        }
    }

    private void addOrderLineItem(final OrderLineItem orderLineItem) {
        orderLineItem.dependOn(this);
        this.orderLineItems.add(orderLineItem);
    }

    public void changeOrderStatus(final OrderStatus orderStatus) {
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderTableId=" + orderTable.getId() +
                ", orderStatus='" + orderStatus + '\'' +
                ", orderedTime=" + orderedTime +
                ", orderLineItems.size=" + orderLineItems.size() +
                '}';
    }
}
