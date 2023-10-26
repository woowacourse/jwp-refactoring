package kitchenpos.domain;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.CollectionUtils;

@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @CreatedDate
    private LocalDateTime orderedTime;

    @OneToMany(mappedBy = "order")
    private List<OrderLineItem> orderLineItems;


    public Order(OrderTable orderTable, OrderStatus orderStatus) {
        validateOrderTable(orderTable);
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
    }

    private void validateOrderTable(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        validateOrderStatus();
        this.orderStatus = orderStatus;
    }

    private void validateOrderStatus() {
        if (Objects.equals(this.orderStatus, OrderStatus.COMPLETION)) {
            throw new IllegalArgumentException();
        }
    }

    public void setOrderLineItems(final List<OrderLineItem> orderLineItems) {
        validateOrderLineItems(orderLineItems);
        this.orderLineItems = orderLineItems;
    }

    private void validateOrderLineItems(List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }
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

    public Long getId() {
        return id;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }

}
