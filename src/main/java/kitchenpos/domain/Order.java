package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

@Table(name = "ORDER_DETAILS")
@Entity
public class Order {

    @GeneratedValue
    @Id
    private Long id;

    @ManyToOne
    private OrderTable orderTable;

    @OneToMany(cascade = CascadeType.REMOVE)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;

    public Order() {
    }

    public Order(OrderTable orderTable, List<OrderLineItem> orderLineItems, OrderStatus orderStatus, LocalDateTime orderedTime) {
        validate(orderTable, orderLineItems);
        this.orderTable = orderTable;
        this.orderLineItems = orderLineItems;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
    }

    private void validate(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        if(orderTable.isEmpty()){
            throw new IllegalArgumentException();
        }

        if(orderLineItems.isEmpty()){
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
        if (OrderStatus.COMPLETION == this.orderStatus) {
            throw new IllegalArgumentException();
        }
        this.orderStatus = orderStatus;
    }

    public void setOrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }
}
