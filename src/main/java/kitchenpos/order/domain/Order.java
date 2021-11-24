package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderTableId;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems;

    public Order() {
    }

    public Order(Long orderTableId) {
        this(null, orderTableId, OrderStatus.COOKING, LocalDateTime.now(), new ArrayList<>());
    }

    public Order(Long orderTableId, List<OrderLineItem> orderLineItems) {
        this(null, orderTableId, OrderStatus.COOKING, LocalDateTime.now(), orderLineItems);
    }

    public Order(Long id, Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = new OrderLineItems(orderLineItems);
    }

    public void addOrderLineItem(List<OrderLineItem> orderLineItems) {
        this.orderLineItems = OrderLineItems.from(orderLineItems);
    }

    public boolean isStatus(OrderStatus status) {
        return this.orderStatus == status;
    }

    public void changeOrderStatus(String orderStatus) {
        OrderStatus status = OrderStatus.valueOf(orderStatus);
        if (OrderStatus.isCompletion(this.orderStatus)) {
            throw new IllegalArgumentException("계산 완료된 주문의 상태는 변경할 수 없습니다.");
        }
        this.orderStatus = status;
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public OrderLineItems getOrderLineItems() {
        return orderLineItems;
    }

    public List<OrderLineItem> getOrderLineItemLists() {
        return orderLineItems.getOrderLineItems();
    }
}
