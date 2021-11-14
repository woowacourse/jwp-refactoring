package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import org.hibernate.annotations.CreationTimestamp;

@Entity(name = "Orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long orderTableId;
    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    @CreationTimestamp
    private LocalDateTime orderedTime;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
    private List<OrderLineItem> orderLineItem = new ArrayList<>();

    public Order() {

    }

    public Order(Long orderTableId, OrderStatus orderStatus) {
        this(orderTableId, orderStatus, new ArrayList<>());
    }

    public Order(Long orderTableId, OrderStatus orderStatus,
        List<OrderLineItem> orderLineItem) {
        this(null, orderTableId, orderStatus, orderLineItem);
    }

    public Order(Long id, Long orderTableId, OrderStatus orderStatus) {
        this(id, orderTableId, orderStatus, new ArrayList<>());
    }

    public Order(Long id, Long orderTableId, OrderStatus orderStatus,
        List<OrderLineItem> orderLineItem) {
        this.id = id;
        this.orderTableId = orderTableId;
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

    public Long getOrderTableId() {
        return orderTableId;
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
