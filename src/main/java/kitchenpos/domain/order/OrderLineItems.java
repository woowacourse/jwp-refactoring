package kitchenpos.domain.order;

import java.util.Collections;
import java.util.List;

public class OrderLineItems {

    private List<OrderLineItem> orderLineItems;

    public OrderLineItems(final List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return Collections.unmodifiableList(orderLineItems);
    }

    public void updateOrder(final Order order) {
        for (final OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.updateOrder(order);
        }
    }
}
