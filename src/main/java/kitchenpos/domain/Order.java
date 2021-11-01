package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import org.hibernate.annotations.CreationTimestamp;

@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private OrderTable orderTable;
    @Enumerated
    private OrderStatus orderStatus;

    @CreationTimestamp
    private LocalDateTime orderedTime;

    @OneToMany(fetch = FetchType.LAZY)
    private final List<OrderLineItem> orderLineItems = new ArrayList<>();

    public Order() {

    }

    public Order(OrderTable orderTable, OrderStatus orderStatus) {
        this(orderTable, orderStatus, new ArrayList<>());
    }

    public Order(OrderTable orderTable, OrderStatus orderStatus,
        List<OrderLineItem> orderLineItems) {
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
        this.orderLineItems.addAll(orderLineItems);
    }

    public void addOrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems.addAll(orderLineItems);
    }

    public void checkOrderStatus() {
        if (Objects.equals(OrderStatus.COMPLETION, this.orderStatus)) {
            throw new IllegalArgumentException();
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

    public void changeOrderStatus(OrderStatus orderStatus) {
        checkOrderStatus();
        this.orderStatus = orderStatus;
    }
}
