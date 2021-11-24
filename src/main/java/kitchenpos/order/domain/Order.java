package kitchenpos.order.domain;

import kitchenpos.table.domain.OrderTable;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime orderedTime;

    protected Order() {
    }

    private Order(OrderTable orderTable) {
        this(null, orderTable, OrderStatus.COOKING, LocalDateTime.now());
    }

    private Order(Long id, OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime) {
        validateEmptyOrderTable(orderTable);
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
    }

    public static Order create(OrderTable orderTable) {
        return new Order(orderTable);
    }

    public static Order create(Long id, OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime) {
        return new Order(id, orderTable, orderStatus, orderedTime);
    }

    private void validateEmptyOrderTable(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException(orderTable.getId() + " 테이블은 비어있어 주문할 수 없습니다.");
        }
    }

    public boolean isCompletion() {
        return orderStatus.isCompletion();
    }

    public boolean isNotCompletion() {
        return orderStatus.isNotCompletion();
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Long getId() {
        return id;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }

    public Long getOrderTableId() {
        return orderTable.getId();
    }

    public String getOrderStatus() {
        return orderStatus.name();
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }
}
