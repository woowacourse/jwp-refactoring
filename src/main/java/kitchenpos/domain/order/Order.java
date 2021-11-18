package kitchenpos.domain.order;

import kitchenpos.domain.OrderTable;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private OrderTable orderTable;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @CreatedDate
    private LocalDateTime orderedTime;

    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected Order() {
    }

    public Order(OrderTable orderTable, OrderStatus orderStatus) {
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
    }

    public Order(OrderTable orderTable, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }


    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(final OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public void setOrderedTime(final LocalDateTime orderedTime) {
        this.orderedTime = orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public void changeOrderLineItems(final List<OrderLineItem> orderLineItems) {
        for (OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.setOrder(this);
        }
        this.orderLineItems = orderLineItems;
    }
}
