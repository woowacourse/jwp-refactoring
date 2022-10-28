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

    private Optional<OrderLineItem> findMenu(final Long menuId) {
        return orderLineItems.stream()
                .filter(orderLineItem -> orderLineItem.isSameMenu(menuId))
                .findAny();
    }

    public void addMenu(final Long menuId, final long quantity) {
        final Optional<OrderLineItem> orderLineItem = findMenu(menuId);
        orderLineItem.ifPresentOrElse(
                findOrderLineItem -> findOrderLineItem.addQuantity(quantity),
                () -> orderLineItems.add(new OrderLineItem(this.id, menuId, 1))
        );
    }

    public void addMenu(final Long menuId) {
        addMenu(menuId, 1);
    }

    public void addMenu(final OrderLineItem savedOrderItem) {
        addMenu(savedOrderItem.getMenuId(), savedOrderItem.getQuantity());
    }

    public void changeOrderStatus(final String orderStatus) {
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
        return orderLineItems;
    }
}
