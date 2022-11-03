package kitchenpos.order.domain;

import org.springframework.util.CollectionUtils;

import java.util.List;

public class OrderLineItems {

    private final List<OrderLineItem> orderLineItems;

    public OrderLineItems(final List<OrderLineItem> orderLineItems) {
        validateSize(orderLineItems);
        this.orderLineItems = orderLineItems;
    }

    private void validateSize(final List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }
    }
}
