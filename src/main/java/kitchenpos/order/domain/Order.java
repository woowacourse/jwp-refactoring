package kitchenpos.order.domain;

import kitchenpos.order.application.OrderValidator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_table_id")
    private Long orderTableId;

    private String orderStatus;

    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems;

    protected Order() {
    }

    public Order(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = new OrderLineItems(orderLineItems);
    }

    public Order(Long orderTableId, List<OrderLineItem> orderLineItems) {
        this(null, orderTableId, OrderStatus.COOKING.name(), LocalDateTime.now(), orderLineItems);
    }

    public Order(String orderStatus) {
        this(null, null, OrderStatus.valueOf(orderStatus).name(), null, null);
    }

    public void validate(OrderValidator orderValidator) {
        orderValidator.validate(this);
    }

    public Long getId() {
        return id;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.getOrderLineItems();
    }

    public List<Long> getMenuIds() {
        return orderLineItems.getMenuIds();
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public void enrollId(Long id) {
        this.id = id;
    }

    public void enrollOrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems = new OrderLineItems(orderLineItems);
    }

    public void changeStatus(String status) {
        if (Objects.equals(OrderStatus.COMPLETION.name(), orderStatus)) {
            throw new IllegalArgumentException("COMPLETION 상태인 Order는 상태를 변경할 수 없습니다.");
        }
        this.orderStatus = status;
    }
}
