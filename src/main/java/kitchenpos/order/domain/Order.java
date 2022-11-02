package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import kitchenpos.order.exception.AlreadyCompletionOrderStatusException;
import kitchenpos.order.validator.OrderValidator;

@Entity(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderTableId;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime orderedTime;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected Order() {
    }

    private Order(Long orderTableId, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = LocalDateTime.now();
        this.orderLineItems = orderLineItems;
        for (OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.belong(this);
        }
    }

    public static Order newOrder(Long orderTableId, List<OrderLineItem> orderLineItems, OrderValidator orderValidator) {
        orderValidator.validateCreation(orderTableId, orderLineItems);
        return new Order(orderTableId, OrderStatus.COOKING, orderLineItems);
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

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        validateCompletionStatus();
        this.orderStatus = orderStatus;
    }

    private void validateCompletionStatus() {
        if (orderStatus.isCompletion()) {
            throw new AlreadyCompletionOrderStatusException();
        }
    }

    public boolean isNotCompletionOrderStatus() {
        return !orderStatus.isCompletion();
    }

    public void addOrderLineItem(OrderLineItem orderLineItem) {
        orderLineItems.add(orderLineItem);
        orderLineItem.belong(this);
    }
}
