package kitchenpos.order.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "ORDER_DETAILS")
@Entity
public class Order {

    @GeneratedValue
    @Id
    private Long id;
    private Long orderTableId;

    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "ORDER_DETAILS_ID")
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;

    public Order() {
    }

    public Order(Long orderTableId, List<OrderLineItem> orderLineItems, OrderStatus orderStatus) {
        if (orderLineItems.isEmpty()) {
            throw new IllegalArgumentException("주문 항목이 적절하지 않습니다.");
        }
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
        this.orderStatus = orderStatus;
        this.orderedTime = LocalDateTime.now();
    }

    public static Order cooking(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        return new Order(orderTable.getId(), orderLineItems, OrderStatus.COOKING);
    }

    public void changeStatus(OrderStatus orderStatus) {
        if (this.orderStatus.isCompletion()) {
            throw new IllegalArgumentException("완료된 주문은 주문 상태를 변경할 수 없습니다.");
        }
        this.orderStatus = orderStatus;
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }
}
