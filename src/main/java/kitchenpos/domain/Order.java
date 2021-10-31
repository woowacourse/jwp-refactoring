package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import org.hibernate.annotations.CreationTimestamp;

@Entity
public class Order {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private OrderTable orderTable;
    @Enumerated
    private OrderStatus orderStatus;

    @CreationTimestamp
    private LocalDateTime orderedTime;

    @OneToMany(fetch = FetchType.LAZY)
    private List<OrderLineItem> orderLineItems;

    public Order(OrderTable orderTable, OrderStatus orderStatus) {
        this(orderTable, orderStatus, new ArrayList<>());
    }

    public Order(OrderTable orderTable, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        this(null, orderTable, orderStatus, orderLineItems);
    }

    public Order(Long id, OrderTable orderTable, OrderStatus orderStatus) {
        this(id, orderTable, orderStatus, new ArrayList<>());
    }

    public Order(Long id, OrderTable orderTable, OrderStatus orderStatus,
        List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
    }

    public void addOrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems.addAll(orderLineItems);
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

    public void changeOrderStatus(String name) {
        this.orderStatus = OrderStatus.valueOf(name);
    }
}
