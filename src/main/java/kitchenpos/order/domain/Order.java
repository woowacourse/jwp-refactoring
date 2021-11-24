package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Table(name = "orders")
@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderTableId;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "order")
    private List<OrderLineItem> orderLineItems;

    private LocalDateTime orderedTime;

    public Order(Long id, Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime,
        List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;

        for (OrderLineItem item : orderLineItems) {
            item.belongsTo(this);
        }
    }

    public Order(Long orderTableId, List<OrderLineItem> orderLineItems) {
        this(null, orderTableId, OrderStatus.COOKING, LocalDateTime.now(), orderLineItems);
    }

    public Order() {
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        if (this.orderStatus.isCompleted()) {
            throw new IllegalStateException("주문 상태가 이미 완료되었습니다.");
        }
        this.orderStatus = orderStatus;
    }

    public void validateCompleted() {
        if (!orderStatus.isCompleted()) {
            throw new IllegalStateException("주문 상태가 완료되지 않았습니다.");
        }
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

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }
}
