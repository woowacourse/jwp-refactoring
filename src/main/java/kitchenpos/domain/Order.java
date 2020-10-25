package kitchenpos.domain;

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

    private String orderStatus;

    @CreatedDate
    private LocalDateTime orderedTime;

    public Order() {
    }

    public Order(Long id) {
        this.id = id;
    }

    public Order(OrderTable orderTable, String orderStatus) {
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
    }

    public Long getContainOrderTableId() {
        return this.orderTable.getId();
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus.name();
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }

    public void setOrderTable(OrderTable orderTable) {
        this.orderTable = orderTable;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public void setOrderedTime(LocalDateTime orderedTime) {
        this.orderedTime = orderedTime;
    }

    public boolean isCompleteOrder() {
        return Objects.equals(OrderStatus.COMPLETION.name(), orderStatus);
    }
}
