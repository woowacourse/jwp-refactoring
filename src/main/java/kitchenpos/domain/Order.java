package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Table(name = "orders")
@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_table_id", foreignKey = @ForeignKey(name = "fk_orders_order_table"))
    private OrderTable orderTable;

    @Enumerated
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "order")
    private List<OrderLineItem> orderLineItems;

    private LocalDateTime orderedTime;

    public Order(Long id, OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime,
        List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public Order(OrderTable orderTable, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        this(null, orderTable, orderStatus, LocalDateTime.now(), orderLineItems);
    }

    public Order() {
    }

    public static Order newOf(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        Order order = new Order(orderTable, OrderStatus.COOKING, orderLineItems);
        orderTable.addOrder(order);
        for (OrderLineItem item : orderLineItems) {
            item.belongsTo(order);
        }
        return order;
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        if (this.orderStatus.isCompleted()) {
            throw new IllegalStateException("주문 상태가 이미 완료되었습니다.");
        }
        this.orderStatus = orderStatus;
    }

    public void validateNotCompleted() {
        if (!orderStatus.isCompleted()) {
            throw new IllegalStateException("주문 상태가 완료되지 않았습니다.");
        }
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

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }
}
