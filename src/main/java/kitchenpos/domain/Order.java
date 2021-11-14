package kitchenpos.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "ORDER_DETAILS")
@Entity
public class Order {

    @GeneratedValue
    @Id
    private Long id;

    @ManyToOne
    private OrderTable orderTable;

    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "ORDER_DETAILS_ID")
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;

    public Order() {
    }

    public Order(OrderTable orderTable, List<OrderLineItem> orderLineItems, OrderStatus orderStatus) {
        validate(orderTable, orderLineItems);
        this.orderTable = orderTable;
        this.orderLineItems = orderLineItems;
        this.orderStatus = orderStatus;
        this.orderedTime = LocalDateTime.now();
    }

    public static Order cooking(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        return new Order(orderTable, orderLineItems, OrderStatus.COOKING);
    }

    private void validate(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        if (orderTable.isEmpty() || orderLineItems.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public void changeStatus(OrderStatus orderStatus) {
        if (completed()) throw new IllegalArgumentException();
        this.orderStatus = orderStatus;
    }

    private boolean completed() {
        return OrderStatus.isCompletion(orderStatus);
    }

    public void setOrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }
}
