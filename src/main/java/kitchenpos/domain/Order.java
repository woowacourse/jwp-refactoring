package kitchenpos.domain;

import kitchenpos.exception.IllegalOrderStatusException;
import kitchenpos.exception.InvalidOrderLineItemException;
import org.springframework.util.CollectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private OrderTable orderTable;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime orderedTime;

    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    public Order() {
    }

    public Order(final OrderTable orderTable) {
        this(null, orderTable);
    }

    public Order(final Long id, final OrderTable orderTable) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = OrderStatus.COOKING;
        this.orderedTime = LocalDateTime.now();
    }

    public void initOrderLineItem(final List<OrderLineItem> orderLineItems){
        validateOrderLineItems(orderLineItems);
        this.orderLineItems = orderLineItems;
    }

    private void validateOrderLineItems(final List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new InvalidOrderLineItemException();
        }
    }

    public void changeOrderStatus(final OrderStatus orderStatus) {
        validateOrderStatus();
        this.orderStatus = orderStatus;
    }

    private void validateOrderStatus() {
        if (Objects.equals(OrderStatus.COMPLETION, orderStatus)) {
            throw new IllegalOrderStatusException();
        }
    }

    public void validateOrderComplete(){
        if(orderStatus.equals(OrderStatus.MEAL) || orderStatus.equals(OrderStatus.COOKING)){
            throw new IllegalOrderStatusException();
        }
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

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public void setOrderTable(final OrderTable orderTable) {
        this.orderTable = orderTable;
    }

    public void setOrderStatus(final OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setOrderedTime(final LocalDateTime orderedTime) {
        this.orderedTime = orderedTime;
    }

    public void setOrderLineItems(final List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }
}
