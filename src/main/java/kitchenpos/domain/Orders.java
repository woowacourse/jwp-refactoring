package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_table_id", nullable = false)
    private OrderTable orderTable;

    private String orderStatus;

    @Column(nullable = false)
    private LocalDateTime orderedTime;

    protected Orders() {
    }

    public Orders(OrderTable orderTable) {
        this(null, orderTable, null, LocalDateTime.now());
    }

    public Orders(OrderTable orderTable, String orderStatus) {
        this(null, orderTable, orderStatus, LocalDateTime.now());
    }

    public Orders(OrderTable orderTable, String orderStatus, LocalDateTime orderedTime) {
        this(null, orderTable, orderStatus, orderedTime);
    }

    public Orders(Long id, OrderTable orderTable, String orderStatus, LocalDateTime orderedTime) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
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

    public void updateOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Long getOrderTableId() {
        return orderTable.getId();
    }

    public boolean isNotCompleted() {
        return Objects.equals(orderStatus, OrderStatus.MEAL.name()) ||
                Objects.equals(orderStatus, OrderStatus.COOKING.name());
    }
}
