package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Table(name = "orders")
@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_table_id", nullable = false)
    private OrderTable orderTable;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;

    @Column(nullable = false)
    private LocalDateTime orderedTime;

    @OneToMany(mappedBy = "order")
    private List<OrderLineItem> orderLineItems;

    protected Order() {
    }

    public Order(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        this(null, orderTable, OrderStatus.COOKING, LocalDateTime.now(), orderLineItems);
    }

    public Order(
            Long id,
            OrderTable orderTable,
            OrderStatus orderStatus,
            LocalDateTime orderedTime,
            List<OrderLineItem> orderLineItems
    ) {
        if (orderTable.isEmpty()) {
            throw new OrderException("빈 테이블을 등록할 수 없습니다.");
        }

        if (orderLineItems.isEmpty()) {
            throw new OrderException("주문 항목이 없습니다.");
        }

        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;

        for (OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.assignOrder(this);
        }
    }

    public void changeOrderStatus(String orderStatus) {
        OrderStatus.checkIfHas(orderStatus);

        if (this.orderStatus == OrderStatus.COMPLETION) {
            throw new OrderException("이미 완료된 주문입니다.");
        }

        this.orderStatus = OrderStatus.valueOf(orderStatus);
    }

    public void validateOrderStatusIsCompletion() {
        if (orderStatus == OrderStatus.MEAL || orderStatus == OrderStatus.COOKING) {
            throw new OrderException("주문 상태가 주문 완료가 아닙니다.");
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
