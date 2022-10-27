package kitchenpos.domain.order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Order {
    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    public Order(final Long id, final Long orderTableId, final String orderStatus, final LocalDateTime orderedTime) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
    }

    public static Order create(final Long orderTableId, final List<Long> menuIds) {
        final Order order = new Order(null, orderTableId, OrderStatus.COOKING.name(), LocalDateTime.now());
        menuIds.forEach(order::addMenu);
        return order;
    }

    public void addMenu(final Long menuId) {
        final Optional<OrderLineItem> orderLineItem = findMenu(menuId);
        orderLineItem.ifPresentOrElse(
                OrderLineItem::addQuantity,
                () -> orderLineItems.add(new OrderLineItem(menuId, 1))
        );
    }

    private Optional<OrderLineItem> findMenu(final Long menuId) {
        return orderLineItems.stream()
                .filter(orderLineItem -> orderLineItem.isSameMenu(menuId))
                .findAny();
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public void setOrderTableId(final Long orderTableId) {
        this.orderTableId = orderTableId;
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
