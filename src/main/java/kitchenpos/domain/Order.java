package kitchenpos.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Table(name = "orders")
@Entity
public class Order {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "order_table_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private OrderTable orderTable;

    @Column(name = "order_status")
    private String orderStatus;

    @Column(name = "ordered_time")
    private LocalDateTime orderedTime;

    @OneToMany(cascade = CascadeType.PERSIST, orphanRemoval = true, mappedBy = "order")
    private List<OrderLineItem> orderLineItems;

    public Order() {
    }

    public Order(final OrderTable orderTable,
                 final String orderStatus,
                 final LocalDateTime orderedTime,
                 final List<OrderLineItem> orderLineItems
    ) {
        this(null, orderTable, orderStatus, orderedTime, orderLineItems);
    }

    public Order(final Long id,
                 final OrderTable orderTable,
                 final String orderStatus,
                 final LocalDateTime orderedTime,
                 final List<OrderLineItem> orderLineItems
    ) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public void addOrderLineItems(final List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems.stream()
                .map(orderLineItem -> new OrderLineItem(this, orderLineItem.getMenu(), orderLineItem.getQuantity()))
                .collect(Collectors.toList());
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

    public void setId(final Long id) {
        this.id = id;
    }

    public void setOrderTable(final OrderTable orderTable) {
        this.orderTable = orderTable;
    }

    public void setOrderStatus(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setOrderedTime(final LocalDateTime orderedTime) {
        this.orderedTime = orderedTime;
    }
}
