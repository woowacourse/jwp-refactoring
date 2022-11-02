package kitchenpos.order.domain;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.util.CollectionUtils;

public class OrderLineItems {

    private final List<OrderLineItem> orderLineItems;

    public OrderLineItems(final List<OrderLineItem> orderLineItems) {
        validateSize(orderLineItems);

        this.orderLineItems = orderLineItems;
    }

    public List<Long> getMenuIds() {
        return orderLineItems.stream()
            .map(OrderLineItem::getMenuId)
            .distinct()
            .collect(Collectors.toList());
    }

    private void validateSize(final List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }
    }
}
