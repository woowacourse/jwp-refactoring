package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import kitchenpos.exception.OrderIsCompletedException;
import kitchenpos.exception.OrderIsNotCompletedException;
import kitchenpos.exception.OrderLineEmptyException;

@Table(name = "orders")
@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime orderedTime;

    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected Order() {
    }

    public Order(OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime) {
        orderTable.validateIsNotEmpty();
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
    }

    public static Order issue(OrderTable orderTable) {
        return new Order(orderTable, OrderStatus.COOKING, LocalDateTime.now());
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        validateOrderIsNotCompleted();
        this.orderStatus = orderStatus;
    }

    private void validateOrderIsNotCompleted() {
        if (orderStatus.isCompleted()) {
            throw new OrderIsCompletedException();
        }
    }

    public void setupOrderLineItem(List<OrderLineItem> orderLineItems) {
        validateOrderLineNotEmpty(orderLineItems);
        this.orderLineItems = orderLineItems;
    }

    private void validateOrderLineNotEmpty(List<OrderLineItem> orderLineItems) {
        if (orderLineItems.isEmpty()) {
            throw new OrderLineEmptyException();
        }
    }

    public void validateOrderIsCompleted() {
        if (orderStatus.isNotCompleted()) {
            throw new OrderIsNotCompletedException();
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

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
