package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import kitchenpos.domain.exception.OrderException.CompletionOrderException;
import kitchenpos.domain.exception.OrderException.EmptyOrderLineItemsException;
import org.springframework.util.CollectionUtils;

@Entity(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    @Column
    private LocalDateTime orderedTime;
    @OneToMany(cascade = CascadeType.PERSIST)
    private List<OrderLineItem> orderLineItems;

    public Order() {
    }

    private Order(final OrderTable orderTable,
                  final OrderStatus orderStatus,
                  final LocalDateTime orderedTime,
                  final List<OrderLineItem> orderLineItems) {
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
        this.orderLineItems.forEach(orderLineItem -> orderLineItem.setOrder(this));
    }

    public static Order of(final OrderTable orderTable,
                           final List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new EmptyOrderLineItemsException();
        }

        return new Order(orderTable, OrderStatus.COOKING, LocalDateTime.now(), orderLineItems);
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getOrderTableId() {
        return orderTable.getId();
    }

    public void setOrderTableId(final Long orderTableId) {
        this.orderTable = new OrderTable();
        this.orderTable.setTableGroupId(orderTableId);
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void changeOrderStatus(final OrderStatus orderStatus) {
        if (this.orderStatus == OrderStatus.COMPLETION) {
            throw new CompletionOrderException();
        }
        this.orderStatus = orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public void setOrderedTime(final LocalDateTime orderedTime) {
        this.orderedTime = orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public void setOrderLineItems(final List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }
}
