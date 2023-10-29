package kitchenpos.order;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class OrderLineItems {

    private final List<OrderLineItem> orderLineItems;

    public OrderLineItems(final List<OrderLineItem> orderLineItems) {
        validateOrderLineItemsNotEmpty(orderLineItems);
        this.orderLineItems = orderLineItems;
    }

    private void validateOrderLineItemsNotEmpty(final List<OrderLineItem> orderLineItems) {
        if (orderLineItems.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    public void orderedBy(final Order order) {
        if (Objects.isNull(order.getId())) {
            throw new IllegalArgumentException();
        }
        for (final OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.orderedBy(order.getId());
        }
    }

    public List<Long> getMenuIds() {
        return orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
    }

    public int getItemSize() {
        return orderLineItems.size();
    }

    public List<OrderLineItem> getItems() {
        return new ArrayList<>(orderLineItems);
    }
}
