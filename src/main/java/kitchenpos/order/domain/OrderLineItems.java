package kitchenpos.order.domain;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.util.CollectionUtils;

public class OrderLineItems {
    private final List<OrderLineItem> orderLineItems;

    public OrderLineItems(final List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    public List<Long> getOrderLineItemMenuIds() {
        return orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
    }

    public boolean isEmpty() {
        return CollectionUtils.isEmpty(orderLineItems);
    }

    public void addOrderLineItem(final OrderLineItem... orderLineItems) {
        this.orderLineItems.addAll(Arrays.asList(orderLineItems));
    }

    public boolean hasValidMenus(final long registeredMenuCounts) {
        return orderLineItems.size() == registeredMenuCounts;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public void addOrderId(final Long orderId) {
        for (final OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.setOrderId(orderId);
        }
    }
}
