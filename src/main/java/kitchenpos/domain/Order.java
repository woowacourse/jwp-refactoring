package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(nullable = false)
    private LocalDateTime orderedTime;

    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected Order() {
    }

    public Order(OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime) {
        validateTableEmpty(orderTable);
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
    }

    public static Order take(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        return new Order(orderTable, OrderStatus.COOKING, LocalDateTime.now(), orderLineItems);
    }

    private Order(
        OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems
    ) {
        validateTableEmpty(orderTable);
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        addOrderLineItems(orderLineItems);
    }

    private void validateTableEmpty(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("빈 테이블에선 주문을 생성할 수 없습니다.");
        }
    }

    public void addOrderLineItems(List<OrderLineItem> orderLineItems) {
        validateItemSize(orderLineItems);
        orderLineItems.forEach(item -> item.updateOrder(this));
        this.orderLineItems.addAll(orderLineItems);
    }

    private void validateItemSize(List<OrderLineItem> orderLineItems) {
        if (orderLineItems.isEmpty()) {
            throw new IllegalArgumentException("메뉴 상품이 1개 이상 있어야 합니다.");
        }
    }

    public void updateStatus(OrderStatus orderStatus) {
        validateOrderStateComplete();
        this.orderStatus = orderStatus;
    }

    private void validateOrderStateComplete() {
        if (orderStatus.isCompleted()) {
            throw new IllegalArgumentException("완료 주문은 상태를 변경할 수 없습니다.");
        }
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
