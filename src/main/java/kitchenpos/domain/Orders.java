package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import kitchenpos.exception.OrderStatusNotChangeableException;

@Entity
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;

    @Column(name = "order_status")
    private String orderStatus;

    @Column(name = "ordered_time")
    private LocalDateTime orderedTime;

    @OneToMany(mappedBy = "orders")
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    public Orders(OrderTable orderTable, String orderStatus, LocalDateTime orderedTime) {
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
    }

    protected Orders() {
    }

    public void changeOrderStatus(String orderStatus) {
        validateDoesStatusChangeable();
        this.orderStatus = orderStatus;
    }

    private void validateDoesStatusChangeable() {
        if (Objects.equals(orderStatus, OrderStatus.COMPLETION.name())) {
            throw new OrderStatusNotChangeableException();
        }
    }

    public boolean isOrderUnCompleted() {
        return !orderStatus.equals(OrderStatus.COMPLETION.name());
    }

    public Long getId() {
        return id;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
