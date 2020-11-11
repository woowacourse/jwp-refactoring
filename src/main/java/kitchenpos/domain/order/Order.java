package kitchenpos.domain.order;

import kitchenpos.domain.BaseEntity;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@EntityListeners(AuditingEntityListener.class)
@AttributeOverride(name = "id", column = @Column(name = "id"))
@Table(name = "orders")
@Entity
public class Order extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @CreatedDate
    private LocalDateTime orderedTime;

    public Order() {
    }

    public Order(Long id) {
        this.id = id;
    }

    public Order(OrderTable orderTable, OrderStatus orderStatus) {
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
    }

    public Order(OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime) {
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
    }

    public Long getContainOrderTableId() {
        return this.orderTable.getId();
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        if (isCompleteOrder()) {
            throw new IllegalArgumentException();
        }
        this.orderStatus = orderStatus;
    }

    private boolean isCompleteOrder() {
        return Objects.equals(OrderStatus.COMPLETION, orderStatus);
    }

    public OrderTable getOrderTable() {
        return orderTable;
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
}
