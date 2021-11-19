package kitchenpos.domain;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.util.CollectionUtils;

public class OrderLineItems {

    private final List<OrderLineItem> orderLineItems;

    public OrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    public void updateOrderId(Order order) {
        validateNotEmpty();
        for (final OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.setOrderId(order.getId());
        }
    }

    public void validateNotEmpty() {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }
    }

    public void validateSameSize(long size) {
        if (orderLineItems.size() != size) {
            throw new IllegalArgumentException();
        }
    }

    public List<OrderLineItem> toList() {
        return Collections.unmodifiableList(orderLineItems);
    }

    public List<Long> getMenuIds() {
        return orderLineItems.stream()
            .map(OrderLineItem::getMenuId)
            .collect(Collectors.toList());
    }
}
