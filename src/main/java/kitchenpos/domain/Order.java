package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime orderedTime;

    @OneToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            mappedBy = "order")
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected Order() {
    }

    public Order(final OrderTable orderTable, final OrderStatus orderStatus, final List<OrderLineItem> orderLineItems) {
        this(null, orderTable, orderStatus, null, orderLineItems);
    }

    public Order(final Long id, final OrderTable orderTable, final OrderStatus orderStatus,
                 final LocalDateTime orderedTime, final List<OrderLineItem> orderLineItems) {
        validate(orderTable);
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        addOrderLineItems(orderLineItems);
    }

    private void validate(final OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("주문 테이블이 비어있습니다.");
        }
    }

    public void addOrderLineItems(final List<OrderLineItem> orderLineItems) {
        for (final OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.updateOrder(this);
        }
        this.orderLineItems = orderLineItems;
    }

    public void changeOrderStatus(final OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public boolean completed() {
        return Objects.equals(OrderStatus.COMPLETION, orderStatus);
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
}
