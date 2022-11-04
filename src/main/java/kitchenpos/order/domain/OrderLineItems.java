package kitchenpos.order.domain;

import java.util.List;
import org.springframework.util.CollectionUtils;

public class OrderLineItems {

    private final List<OrderLineItem> orderLineItems;

    public OrderLineItems(List<OrderLineItem> orderLineItems) {
        validateEmpty(orderLineItems);
        this.orderLineItems = orderLineItems;
    }

    private void validateEmpty(List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }
    }

    public boolean hasSameMenu() {
        return this.orderLineItems.size() != this.orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .distinct()
                .count();
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
