package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import kitchenpos.domain.validator.OrderValidator;

@Entity(name = "orders")
public class Order {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    private OrderTable orderTable;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    @Transient
    private OrderLineItems orderLineItems;

    protected Order() {
    }

    public Order(String orderStatus) {
        this(null, null, OrderStatus.valueOf(orderStatus), null, null);
    }

    public Order(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        this(null, orderTable, null, null, new OrderLineItems(orderLineItems));
    }

    public Order(OrderTable orderTable, String orderStatus, LocalDateTime orderedTime,
                 List<OrderLineItem> orderLineItems) {
        this(null, orderTable, OrderStatus.valueOf(orderStatus), orderedTime, new OrderLineItems(orderLineItems));
    }

    private Order(Long id, OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime,
                  OrderLineItems orderLineItems) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public void register(OrderValidator orderValidator) {
        orderValidator.validate(this);

        orderStatus = OrderStatus.COOKING;
        orderedTime = LocalDateTime.now();
        orderLineItems.setOrder(this);
    }

    public void changeStatus(OrderStatus orderStatus) {
        if (OrderStatus.COMPLETION.equals(this.orderStatus)) {
            throw new IllegalArgumentException();
        }
        this.orderStatus = orderStatus;
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

    public OrderLineItems getOrderLineItems() {
        return orderLineItems;
    }

    public void setOrderLineItems(final OrderLineItems orderLineItems) {
        this.orderLineItems = orderLineItems;
    }
}
