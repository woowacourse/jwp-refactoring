package kitchenpos.domain.order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.springframework.util.CollectionUtils;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long orderTableId;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    @Column(nullable = false)
    private LocalDateTime orderedTime;

    private OrderLineItems orderLineItems = new OrderLineItems();

    protected Order() {
    }

    private Order(final Long id,
                 final Long orderTableId,
                 final OrderStatus orderStatus,
                 final LocalDateTime orderedTime,
                 final List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = new OrderLineItems(orderLineItems, this);
    }

    public static Order create(final Long orderTableId, final List<OrderLineItem> orderLineItems) {
        validateEmptyMenu(orderLineItems);
        return new Order(null, orderTableId, OrderStatus.COOKING, LocalDateTime.now(), orderLineItems);
    }

    private static void validateEmptyMenu(final List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }
    }

    public void changeOrderStatus(final OrderStatus orderStatus) {
        if (Objects.equals(OrderStatus.COMPLETION, getOrderStatus())) {
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

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.getValues();
    }
}
