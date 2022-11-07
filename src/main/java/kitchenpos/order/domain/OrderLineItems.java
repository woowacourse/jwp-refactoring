package kitchenpos.order.domain;

import java.util.List;
import java.util.stream.Collectors;

public class OrderLineItems {

    private final List<OrderLineItem> orderLineItems;

    public OrderLineItems(final List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public List<Long> getMenuIds() {
        return orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
    }

    public boolean isEmpty() {
        return orderLineItems.isEmpty();
    }

    public int size() {
        return orderLineItems.size();
    }

    public void setOrderMenuId(final OrderMenus orderMenus) {
        for (final OrderLineItem orderLineItem : orderLineItems) {
            final Long orderMenuId = orderMenus.findIdByMenuId(orderLineItem.getMenuId());
            orderLineItem.setOrderMenuId(orderMenuId);
        }
    }
}
