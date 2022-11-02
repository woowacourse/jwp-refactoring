package kitchenpos.order.domain;

import java.util.List;

import org.springframework.util.CollectionUtils;

public class OrderValidator {
    public void validateOrderLineItems(final List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }
    }
}
