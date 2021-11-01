package kitchenpos.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

@Entity(name = "Orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    private OrderTable orderTable;
    @Enumerated
    private OrderStatus orderStatus;

    @CreationTimestamp
    private LocalDateTime orderedTime;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
    private List<OrderLineItem> orderLineItem = new ArrayList<>();

    public Order() {

    }

    public Order(OrderTable orderTable, OrderStatus orderStatus) {
        this(orderTable, orderStatus, new ArrayList<>());
    }

    public Order(OrderTable orderTable, OrderStatus orderStatus,
        List<OrderLineItem> orderLineItem) {
        this(null, orderTable, orderStatus, orderLineItem);
    }

    public Order(Long id, OrderTable orderTable, OrderStatus orderStatus) {
        this(id, orderTable, orderStatus, new ArrayList<>());
    }

    public Order(Long id, OrderTable orderTable, OrderStatus orderStatus,
        List<OrderLineItem> orderLineItem) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderLineItem.addAll(orderLineItem);
    }

    public void addOrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItem.addAll(orderLineItems);
    }

    public void checkOrderStatus() {
        if (Objects.equals(OrderStatus.COMPLETION, this.orderStatus)) {
            throw new IllegalArgumentException();
        }
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        checkOrderStatus();
        this.orderStatus = orderStatus;
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

    public List<OrderLineItem> getOrderLineItem() {
        return orderLineItem;
    }
}
