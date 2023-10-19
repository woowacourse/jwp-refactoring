package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Table(name = "orders")
@Entity
public class Order {

    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;

    @Enumerated(STRING)
    private OrderStatus orderStatus;

    @Column
    private LocalDateTime orderedTime;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "order_table_id", foreignKey = @ForeignKey(name = "fk_order_to_order_table"))
    private OrderTable orderTable;

    @OneToMany(mappedBy = "order", cascade = PERSIST)
    private List<OrderLineItem> orderLineItems;

    protected Order() {
    }

    private Order(final Long id, final OrderStatus orderStatus, final LocalDateTime orderedTime, final OrderTable orderTable, final List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderTable = orderTable;
        this.orderLineItems = orderLineItems;
    }

    public Order(final OrderStatus orderStatus, final OrderTable orderTable) {
        this(null, orderStatus, LocalDateTime.now(), orderTable, new ArrayList<>());
    }

    public void changeStatus(final OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void addOrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems.addAll(orderLineItems);
    }

    public Long getId() {
        return id;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
