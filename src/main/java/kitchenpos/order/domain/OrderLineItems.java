package kitchenpos.order.domain;

import java.util.Arrays;
import java.util.List;
import org.springframework.util.CollectionUtils;

public class OrderLineItems {
    private final List<OrderLineItem> orderLineItems;

    public OrderLineItems(final List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
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
}
