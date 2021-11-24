package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Table(name = "orders")
@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private OrderTable orderTable;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime orderedTime;

    @OneToMany(mappedBy = "orders")
    private List<OrderLineItem> orderLineItems;

    protected Order() {
    }

    public Order(final OrderTable orderTable, final OrderStatus orderStatus, final List<OrderLineItem> orderLineItems) {
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
    }

    public Order(final OrderTable orderTable, final List<OrderLineItem> orderLineItems) {
        this(null, orderTable, null, null, orderLineItems);
    }

    public Order(final Long id, final OrderTable orderTable, final OrderStatus orderStatus, final LocalDateTime orderedTime,
        final List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public String orderStatus() {
        return orderStatus.getName();
    }

    public void changeOrderStats(final OrderTable orderTable, final OrderStatus status, final LocalDateTime time) {
        this.orderTable = orderTable;
        this.orderStatus = status;
        this.orderedTime = time;
    }

    public void changeOrderStatus(final OrderStatus status) {
        orderStatus = status;
    }

    public void changeOrderLineItems(final List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    public Long orderTableId() {
        return orderTable.getId();
    }

    public void clearId() {
        this.id = null;
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

    public void validateStatus(final OrderStatus status) {
        if (Objects.equals(this.orderStatus, status)) {
            throw new IllegalArgumentException();
        }
    }
}
