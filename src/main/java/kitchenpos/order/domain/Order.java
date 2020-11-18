package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.ordertable.domain.OrderTable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "ORDERS")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime orderedTime;

    @ManyToOne
    private OrderTable orderTable;

    public Order() {
    }

    public Order(OrderTable orderTable, OrderStatus orderStatus) {
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = LocalDateTime.now();
    }

    public Order(OrderTable orderTable) {
        this(orderTable, OrderStatus.COOKING);
        validate();
    }

    private void validate() {
        if (this.orderTable.isEmpty()) {
            throw new IllegalArgumentException("빈 테이블에는 주문을 등록할 수 없습니다.");
        }
    }

    public boolean isUngroupable() {
        return this.orderStatus.canUngroup();
    }

    public void changeStatus(String orderStatus) {
        if (this.orderStatus.isCompletion()) {
            throw new IllegalArgumentException("주문 상태가 계산 완료인 경우 변경할 수 없습니다.");
        }
        this.orderStatus = OrderStatus.valueOf(orderStatus);
    }

    public OrderLineItem createOrderLineItem(Long quantity, Menu menu) {
        return new OrderLineItem(quantity, this, menu);
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
}
