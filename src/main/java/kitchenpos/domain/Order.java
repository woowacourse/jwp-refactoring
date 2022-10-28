package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.util.CollectionUtils;

/**
 * 매장에서 발생하는 주문
 */
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_table_id")
    private Long orderTableId;

    @Column(name = "order_status")
    private String orderStatus;

    @Column(name = "ordered_time")
    private LocalDateTime orderedTime;

    @OneToMany(mappedBy = "orderId")
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected Order() {
    }

    public Order(final Long id, final Long orderTableId, final String orderStatus, final LocalDateTime orderedTime,
        final List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = List.copyOf(orderLineItems);
        validateOrderLineItems();
    }

    public Order(final Long id, final Long orderTableId, final String orderStatus, final LocalDateTime orderedTime) {
        this(id, orderTableId, orderStatus, orderedTime, Collections.emptyList());
    }

    public Order(final Long orderTableId, final List<OrderLineItem> orderLineItems) {
        this(null, orderTableId, OrderStatus.COOKING.name(), LocalDateTime.now(), orderLineItems);
    }

    private void validateOrderLineItems() {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }
    }

    public void setIdToOrderLineItems() {
        for (final OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.setOrderId(id);
        }
    }

    public void changeStatus(final String orderStatus) {
        if (Objects.equals(OrderStatus.COMPLETION.name(), this.orderStatus)) {
            throw new IllegalArgumentException();
        }
        this.orderStatus = orderStatus;
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return List.copyOf(orderLineItems);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        final Order order = (Order)o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
