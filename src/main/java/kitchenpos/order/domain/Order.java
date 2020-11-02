package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import kitchenpos.table.domain.Table;

@Entity
@EntityListeners(AuditingEntityListener.class)
@javax.persistence.Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Table table;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @CreatedDate
    private LocalDateTime orderedTime;

    @OneToMany(mappedBy = "order")
    private List<OrderLineItem> orderLineItems;

    public Order() {
    }

    public Order(Long id, Table table, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        if (table.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.id = id;
        this.table = table;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
    }

    public Order(Table table, OrderStatus orderStatus) {
        this(null, table, orderStatus, Collections.emptyList());
    }

    public Table getTable() {
        return table;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public void changeId(Long id) {
        this.id = id;
    }

    public void changeOrderTableId(Table table) {
        this.table = table;
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        if (orderStatus.isCompletion()) {
            throw new IllegalArgumentException();
        }
        this.orderStatus = orderStatus;
    }

    public void changeOrderedTime(LocalDateTime localDateTime) {
        this.orderedTime = localDateTime;
    }

    public void changeOrderLineItems(List<OrderLineItem> savedOrderLineItems) {
        this.orderLineItems = savedOrderLineItems;
    }
}
