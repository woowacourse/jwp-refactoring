package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.*;

@Entity(name = "orders")
public class Order extends BaseEntity {

    @ManyToOne
    private OrderTable orderTable;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime orderedTime;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    private List<OrderLineItem> orderLineItems;

    public Order() {}

    public Order(Long id, OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        super(id);
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
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

    public boolean isCompletion() {
        return OrderStatus.COMPLETION == orderStatus;
    }

    public void changeStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void addOrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems.addAll(orderLineItems);
    }
}
