package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.util.CollectionUtils;

public class Order {

    private final Long id;
    private final Long orderTableId;
    private final OrderStatus orderStatus;
    private final LocalDateTime orderedTime;
    private List<OrderLineItem> orderLineItems;

    public Order(final Long orderTableId, final OrderStatus orderStatus, final LocalDateTime orderedTime,
                 final List<OrderLineItem> orderLineItems) {
        this(null, orderTableId, orderStatus, orderedTime, orderLineItems);
    }

    private Order(final Long id, final Long orderTableId, final OrderStatus orderStatus,
                  final LocalDateTime orderedTime) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
    }

    public Order(final Long id, final Long orderTableId, final OrderStatus orderStatus, final LocalDateTime orderedTime,
                 final List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }

        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static Order forEntity(final Long id, final Long orderTableId, final OrderStatus orderStatus,
                                  final LocalDateTime orderedTime) {
        return new Order(id, orderTableId, orderStatus, orderedTime);
    }

    public static Order proceed(final Long orderTableId, final List<OrderLineItem> items) {
        return new Order(null, orderTableId, OrderStatus.COOKING, LocalDateTime.now(), items);
    }

    public Order changeOrderStatus(final OrderStatus orderStatus) {
        if (Objects.equals(OrderStatus.COMPLETION, this.orderStatus)) {
            throw new IllegalArgumentException();
        }

        return new Order(id, orderTableId, orderStatus, orderedTime, orderLineItems);
    }

    public int getItemSize() {
        return orderLineItems.size();
    }

    public List<Long> getMenuIds() {
        return orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
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
}
