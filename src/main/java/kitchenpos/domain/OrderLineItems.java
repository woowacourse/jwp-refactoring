package kitchenpos.domain;

import java.util.List;
import java.util.stream.Collectors;

public class OrderLineItems {

    private final List<OrderLineItem> orderLineItems;

    public OrderLineItems(List<OrderLineItem> orderLineItems) {
        validate(orderLineItems);
        this.orderLineItems = orderLineItems;
    }

    private void validate(List<OrderLineItem> orderLineItems) {
        if (orderLineItems.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    public List<Menu> toMenus() {
        return orderLineItems.stream()
            .map(o -> o.getMenu())
            .collect(Collectors.toList());
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
