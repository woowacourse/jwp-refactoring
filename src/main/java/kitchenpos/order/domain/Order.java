package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import kitchenpos.common.exception.CustomErrorCode;
import kitchenpos.common.exception.DomainLogicException;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime orderedTime;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id", nullable = false, updatable = false)
    private List<OrderLineItem> orderLineItems;

    protected Order() {
    }

    public Order(OrderStatus orderStatus, LocalDateTime orderedTime,
                 List<OrderLineItem> orderLineItems) {
        this(null, null, orderStatus, orderedTime, orderLineItems);
    }

    public Order(Long id, OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime,
                 List<OrderLineItem> orderLineItems) {
        validateOrderLineItemsNotEmpty(orderLineItems);
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    private void validateOrderLineItemsNotEmpty(final List<OrderLineItem> items) {
        if (items == null || items.isEmpty()) {
            throw new DomainLogicException(CustomErrorCode.ORDER_ITEM_EMPTY_ERROR);
        }
    }

    public void changeStatus(final OrderStatus status) {
        if (status.isSame(OrderStatus.MEAL)) {
            this.orderStatus = this.orderStatus.meal();
            return;
        }
        this.orderStatus = this.orderStatus.complete();
    }

    public void setTable(final OrderTable table) {
        this.orderTable = table;
    }

    public void meal() {
        this.orderStatus = this.orderStatus.meal();
    }

    public void complete() {
        this.orderStatus = this.orderStatus.complete();
    }

    public boolean isCompleted() {
        return this.orderStatus.isSame(OrderStatus.COMPLETION);
    }

    public Long getId() {
        return id;
    }

    public Long getTableId() {
        return this.orderTable.getId();
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
}
