package kitchenpos.domain.order;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(STRING)
    private OrderStatus orderStatus;

    private LocalDateTime orderedTime;

    @JoinColumn(name = "order_table_id")
    @ManyToOne(fetch = LAZY)
    private OrderTable orderTable;

    @OneToMany(mappedBy = "order")
    private List<OrderLineItem> orderLineItems;

    public Order() {
    }

    public Order(OrderStatus orderStatus,
                 LocalDateTime orderedTime,
                 OrderTable orderTable,
                 List<OrderLineItem> orderLineItems) {
        this(null, orderStatus, orderedTime, orderTable, orderLineItems);
    }

    public Order(Long id,
                 OrderStatus orderStatus,
                 LocalDateTime orderedTime,
                 OrderTable orderTable,
                 List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderTable = orderTable;
        this.orderLineItems = orderLineItems;
    }

    public void mapOrderLineItems(List<OrderLineItem> orderLineItems) {

        this.orderLineItems = orderLineItems;

        for (OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.setOrder(this);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public void setOrderedTime(LocalDateTime orderedTime) {
        this.orderedTime = orderedTime;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }

    public void setOrderTable(OrderTable orderTable) {
        this.orderTable = orderTable;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public void setOrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderStatus=" + orderStatus +
                ", orderedTime=" + orderedTime +
                '}';
    }
}

