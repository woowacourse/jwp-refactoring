package kitchenpos.order.domain;

import static kitchenpos.exception.ExceptionType.INVALID_CHANGE_ORDER_STATUS_EXCEPTION;
import static kitchenpos.exception.ExceptionType.INVALID_TABLE_UNGROUP_EXCEPTION;
import static kitchenpos.exception.ExceptionType.NOT_FOUND_ORDER_LINE_ITEM_EXCEPTION;
import static kitchenpos.order.domain.OrderStatus.COMPLETION;
import static kitchenpos.order.domain.OrderStatus.COOKING;
import static kitchenpos.order.domain.OrderStatus.valueOf;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import kitchenpos.exception.CustomIllegalArgumentException;
import org.springframework.util.CollectionUtils;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long orderTableId;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column
    private LocalDateTime orderedTime;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "order_id", nullable = false)
    private List<OrderLineItem> orderLineItems;

    public Order() {
    }

    public Order(final Long orderTableId, final String orderStatus, final LocalDateTime orderedTime,
                 final List<OrderLineItem> orderLineItems) {
        this(null, orderTableId, orderStatus, orderedTime, orderLineItems);
    }

    public Order(final Long id, final Long orderTableId, final String orderStatus, final LocalDateTime orderedTime) {
        this(id, orderTableId, orderStatus, orderedTime, new ArrayList<>());
    }

    public Order(final Long id, final Long orderTableId, final String orderStatus, final LocalDateTime orderedTime,
                 final List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = valueOf(orderStatus);
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
        validEmpty();
    }

    public void changeOrderStatus(final String orderStatus) {
        if (Objects.equals(COMPLETION, this.orderStatus)) {
            throw new CustomIllegalArgumentException(INVALID_CHANGE_ORDER_STATUS_EXCEPTION);
        }
        this.orderStatus = valueOf(orderStatus);
    }

    public void validExistOrderStatus() {
        if (orderStatus.equals(COOKING) || orderStatus.equals(OrderStatus.MEAL)) {
            throw new CustomIllegalArgumentException(INVALID_TABLE_UNGROUP_EXCEPTION);
        }
    }

    private void validEmpty() {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new CustomIllegalArgumentException(NOT_FOUND_ORDER_LINE_ITEM_EXCEPTION);
        }
    }


    public void setOrderLineItems(final List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus.name();
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
