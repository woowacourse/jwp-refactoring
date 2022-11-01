package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import kitchenpos.exception.CompletedOrderStatusChangeException;
import kitchenpos.exception.NotContainsOrderLineItemException;
import kitchenpos.exception.OrderTableEmptyException;
import org.springframework.util.CollectionUtils;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;
    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected Order() {
    }

    public Order(final Long id, final OrderTable orderTable, final OrderStatus orderStatus, final LocalDateTime orderedTime,
                 final List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new NotContainsOrderLineItemException();
        }
        if (orderTable.isEmpty()) {
            throw new OrderTableEmptyException();
        }
        orderLineItems.forEach(orderLineItem -> orderLineItem.setOrder(this));
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static Order of(final OrderTable orderTable, final List<OrderLineItem> orderLineItems) {
        return new Order(null, orderTable, OrderStatus.COOKING, LocalDateTime.now(), orderLineItems);
    }

    public void addOrderLineItem(final OrderLineItem orderLineItem) {
        orderLineItems.add(orderLineItem);
    }

    public boolean isNotComplete() {
        return orderStatus != OrderStatus.COMPLETION;
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

    public void setOrderStatus(final OrderStatus orderStatus) {
        if (Objects.equals(OrderStatus.COMPLETION, this.orderStatus)) {
            throw new CompletedOrderStatusChangeException();
        }
        this.orderStatus = orderStatus;
    }
}
