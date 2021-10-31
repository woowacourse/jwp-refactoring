package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Entity;
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
    private String orderStatus;
    private LocalDateTime orderedTime;
    @Transient
    private OrderLineItems orderLineItems;

    protected Order() {
    }

    public Order(String orderStatus) {
        this(null, null, orderStatus, null, null);
    }

    public Order(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        this(orderTable, null, null, orderLineItems);
    }

    public Order(OrderTable orderTable, String orderStatus, LocalDateTime orderedTime,
                 List<OrderLineItem> orderLineItems) {
        this(null, orderTable, orderStatus, orderedTime, new OrderLineItems(orderLineItems));
    }

    public Order(Long id, OrderTable orderTable, String orderStatus, LocalDateTime orderedTime,
                 OrderLineItems orderLineItems) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public void register(OrderValidator orderValidator) {
        orderValidator.validate(this);

        orderStatus = OrderStatus.COOKING.name();
        orderedTime = LocalDateTime.now();
        orderLineItems.setOrder(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }

    public void setOrderTable(OrderTable orderTable) {
        this.orderTable = orderTable;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(final String orderStatus) {
        this.orderStatus = orderStatus;
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
