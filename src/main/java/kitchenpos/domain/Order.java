package kitchenpos.domain;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;
    @Enumerated(value = STRING)
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    @OneToMany(mappedBy = "order")
    private List<OrderLineItem> orderLineItems;

    protected Order() {
    }

    public Order(Long id, OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime,
        List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTable = orderTable ;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems == null ? new ArrayList<>() : orderLineItems;
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void addOrderLineItem(OrderLineItem orderLineItem) {
        this.orderLineItems.add(orderLineItem);
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
